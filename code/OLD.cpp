#include "linear_regression.hpp"
#include <fstream>
#include <iostream>
#include <cmath>

using namespace std;
using namespace arma;

int main(int argc, char** argv)
{
    cout << "Kshit is gay " << endl;

    ifstream infile("in.dta");
    double x;
    double y;
    double score;
    vector<point> points;
    while (infile >> x >> y >> score)
    {
        point p;
        p.x = x;
        p.y = y;
        p.score = (int) score;
        points.push_back(p);
    }

    ifstream outfile("out.dta");
    vector<point> points_out;
    while (outfile >> x >> y >> score)
    {
        point p;
        p.x = x;
        p.y = y;
        p.score = (int) score;
        points_out.push_back(p);
    }

    LinearRegression linear_regression(points);
    linear_regression.set_points_out(points_out);
    linear_regression.get_weight_matrix();
    double e_classification_in = linear_regression.get_e_classification_in();
    cout << "e_classification_in = " << e_classification_in << endl;
    cout << "\n";
    double e_classification_out = linear_regression.get_e_classification_out();
    cout << "e_classification_out = " << e_classification_out << endl;
    cout << "\n";
    double e_squared_in = linear_regression.get_e_squared_in();
    cout << "e_squared_in = " << e_squared_in << endl;
    cout << "\n";
    double e_squared_out = linear_regression.get_e_squared_out();
    cout << "e_squared_out = " << e_squared_out << endl;
    cout << "\n";
    double e_aug_in = linear_regression.get_e_aug_in();
    cout << "e_aug_in = " << e_aug_in << endl;
    cout << "\n";
    double e_aug_out = linear_regression.get_e_aug_out();
    cout << "e_aug_out = " << e_aug_out << endl;
}

LinearRegression::LinearRegression()
{
}

LinearRegression::LinearRegression(vector<point> input)
{
    num_points = input.size();
    points = input;
    make_points_transformed();
    make_matrices();

    x_matrix.print("x_matrix: ");
    y_matrix.print("y_matrix: ");

}

void LinearRegression::make_points_transformed()
{
    for (int i = 0; i < num_points; i++)
    {
        point p = points[i];
        vector<double> transformed_point = transform_point(p);
        points_transformed.push_back(transformed_point);
    }
}

void LinearRegression::make_points_transformed_out()
{
    for (int i = 0; i < num_points_out; i++)
    {
        point p = points_out[i];
        vector<double> transformed_point = transform_point(p);
        points_transformed_out.push_back(transformed_point);
    }
}

vector<double> LinearRegression::transform_point(point p)
{
    vector<double> transformed_point;
    transformed_point.push_back(1);
    transformed_point.push_back(p.x);
    transformed_point.push_back(p.y);
    transformed_point.push_back(pow(p.x, 2));
    transformed_point.push_back(pow(p.y, 2));
    transformed_point.push_back(p.x * p.y);
    transformed_point.push_back(abs(p.x - p.y));
    transformed_point.push_back(abs(p.x + p.y));
    return transformed_point;
}

void LinearRegression::make_matrices()
{
    for (int i = 0; i < num_points; i++)
    {
        mat x_row(points_transformed[i]);
        x_row = x_row.t();
        x_matrix.insert_rows(i, x_row);

        vector<double> vec;
        vec.push_back(points[i].score);
        mat y_row(vec);
        y_matrix.insert_rows(i, y_row);
    }
}

void LinearRegression::make_matrix_out()
{
    for (int i = 0; i < num_points_out; i++)
    {
        mat x_row(points_transformed_out[i]);
        x_row = x_row.t();
        x_matrix_out.insert_rows(i, x_row);
    }
}

void LinearRegression::set_points_out(vector<point> val)
{
    points_out = val;
    num_points_out = points_out.size();
    make_points_transformed_out();
    make_matrix_out();
}

mat LinearRegression::get_weight_matrix()
{
    if (w_matrix_set == false)
    {
        w_matrix = pinv(x_matrix) * y_matrix;
        w_matrix.print("w_matrix:");
        w_matrix_set = true;
    }
    return w_matrix;
}

double LinearRegression::get_e_classification_in()
{
    mat w_matrix = get_weight_matrix();
    double count = 0;
    for (int i = 0; i < num_points; i++)
    {
        rowvec row = x_matrix.row(i);
        double dot_product = dot(row, w_matrix);
        int sign_dot_product = sign(dot_product);
        if (sign_dot_product != points[i].score)
            count++;
    }
    return count / num_points;
}

double LinearRegression::get_e_classification_out()
{
    mat w_matrix = get_weight_matrix();
    double count = 0;
    for (int i = 0; i < num_points_out; i++)
    {
        rowvec row = x_matrix_out.row(i);
        double dot_product = dot(row, w_matrix);
        int sign_dot_product = sign(dot_product);
        if (sign_dot_product != points_out[i].score)
            count++;
    }
    return count / num_points_out;
}

double LinearRegression::get_e_squared_in()
{
    mat w_matrix = get_weight_matrix();
    double sum = 0;
    for (int i = 0; i < num_points; i++)
    {
        rowvec row = x_matrix.row(i);
        double dot_product = dot(row, w_matrix);
        double squared_error = pow(dot_product - points[i].score, 2);
        sum += squared_error;
    }
    return sum / num_points;
}

double LinearRegression::get_e_squared_out()
{
    mat w_matrix = get_weight_matrix();
    double sum = 0;
    for (int i = 0; i < num_points_out; i++)
    {
        rowvec row = x_matrix_out.row(i);
        double dot_product = dot(row, w_matrix);
        double squared_error = pow(dot_product - points_out[i].score, 2);
        sum += squared_error;
    }
    return sum / num_points_out;
}

double LinearRegression::get_e_aug_in()
{
    mat w_matrix = get_weight_matrix();
    double e_squared_in = get_e_squared_in();
    w_matrix = pow(w_matrix, 2);
    w_matrix.print("w_matrix^2:");
    double sum = accu(w_matrix);
    cout << "sum = " << sum << endl;
    double decay = (sum) * (pow(10, k) / num_points);
    cout << "decay = " << decay << endl;
    return e_squared_in + decay;
}

double LinearRegression::get_e_aug_out()
{
    mat w_matrix = get_weight_matrix();
    double e_squared_out = get_e_squared_out();
    w_matrix = pow(w_matrix, 2);
    w_matrix.print("w_matrix^2:");
    double sum = accu(w_matrix);
    cout << "sum = " << sum << endl;
    double decay = (sum) * (pow(10, k) / num_points_out);
    cout << "decay = " << decay << endl;
    return e_squared_out + decay;
    return 0;
}

int LinearRegression::sign(double num)
{
    if (num > 0) return 1;
    else return -1;
}

LinearRegression::~LinearRegression()
{
}
