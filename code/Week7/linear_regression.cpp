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
    double e_classification_in = linear_regression.get_e_classification_in(false);
    cout << "e_classification_in (without weight decay) = "
        << e_classification_in << endl;
    double e_classification_out = linear_regression.get_e_classification_out(false);
    cout << "e_classification_out (without weight decay) = "
        << e_classification_out << endl;
    double min = 1;
    for (int i = 10; i > -11; i--)
    {
        linear_regression.k = i;
        cout << "k = " << i << endl;
        e_classification_in = linear_regression.get_e_classification_in(true);
        cout << "e_classification_in (with weight decay) = "
            << e_classification_in << endl;
        e_classification_out = linear_regression.get_e_classification_out(true);
        cout << "e_classification_out (with weight decay) = "
            << e_classification_out << endl;
        if (e_classification_out < min)
            min = e_classification_out;
        cout << "" << endl;
    }
    cout << "min = " << min << endl;
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
    }
    return w_matrix;
}

mat LinearRegression::get_weight_matrix_reg()
{
   if (w_matrix_reg_set == false)
   {
       mat I(weight_vector_length, weight_vector_length);
       double lambda = pow(10, k);
       w_matrix_reg = inv((x_matrix.t() * x_matrix + lambda * I.eye())) *
           (x_matrix.t() * y_matrix);
   }
   return w_matrix_reg;
}

double LinearRegression::get_e_classification_in(bool weight_decay)
{
    mat w_matrix;
    if (weight_decay)
        w_matrix = get_weight_matrix_reg();
    else
        w_matrix = get_weight_matrix();
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

double LinearRegression::get_e_classification_out(bool weight_decay)
{
    mat w_matrix;
    if (weight_decay)
        w_matrix = get_weight_matrix_reg();
    else
        w_matrix = get_weight_matrix();
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

int LinearRegression::sign(double num)
{
    if (num > 0) return 1;
    else return -1;
}

LinearRegression::~LinearRegression()
{
}
