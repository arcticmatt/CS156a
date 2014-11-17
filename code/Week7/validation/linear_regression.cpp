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
    while (infile >> x >> y >> score) {
        point p;
        p.x = x;
        p.y = y;
        p.score = (int) score;
        points.push_back(p);
    }
    vector<point> training_points;
    vector<point> validation_points;
    for (int i = 0; i < points.size(); i++) {
        if (i < 25)
            training_points.push_back(points[i]);
        else
            validation_points.push_back(points[i]);
    }

    ifstream outfile("out.dta");
    vector<point> points_out;
    while (outfile >> x >> y >> score) {
        point p;
        p.x = x;
        p.y = y;
        p.score = (int) score;
        points_out.push_back(p);
    }

    for (int i = 4; i <= 8; i++) {
        cout << "dim = " << i << endl;
        LinearRegression linear_regression(training_points, i);
        linear_regression.set_points_validation(validation_points);
        linear_regression.set_points_out(points_out);
        linear_regression.get_weight_matrix();
        //linear_regression.print_matrices();
        double e_classification_in = linear_regression.get_e_classification_in(false);
        cout << "e_classification_in (without weight decay) = "
            << e_classification_in << endl;
        double e_classification_out = linear_regression.get_e_classification_out(false);
        cout << "e_classification_out (without weight decay) = "
            << e_classification_out << endl;
        double e_classification_val =
            linear_regression.get_e_classification_validation(false);
        cout << "e_classification_val (without weight decay) = "
            << e_classification_val << endl;
    }
    cout << "\n\n";
    vector<point> data_points;
    point p1;
    p1.x = -1;
    p1.score = 0;
    point p2;
    p2.x = 4.33;
    p2.score = 1;
    point p3;
    p3.x = 1;
    p3.score = 0;
    data_points.push_back(p1);
    data_points.push_back(p2);
    data_points.push_back(p3);

    for (int i = 1; i <= 2; i++) {
        cout << "dim = " << i << endl;
        LinearRegression linear_regression(data_points, i);
        double e_cross_validation = linear_regression.get_e_cross_validation(false);
        cout << "e_cross_validation (without weight decay) = "
            << e_cross_validation << endl;
    }
}

LinearRegression::LinearRegression() {
}

LinearRegression::LinearRegression(vector<point> input, int dimension) {
    dim = dimension;
    set_points(input);
}

void LinearRegression::make_points_transformed() {
    vector<vector<double> > points_transformed_new;
    for (int i = 0; i < num_points; i++) {
        point p = points[i];
        vector<double> transformed_point = transform_point(p);
        points_transformed_new.push_back(transformed_point);
    }
    points_transformed = points_transformed_new;
}

void LinearRegression::make_points_transformed_out() {
    vector<vector<double> > points_transformed_out_new;
    for (int i = 0; i < num_points_out; i++) {
        point p = points_out[i];
        vector<double> transformed_point = transform_point(p);
        points_transformed_out_new.push_back(transformed_point);
    }
    points_transformed_out = points_transformed_out_new;
}

void LinearRegression::make_points_transformed_validation() {
    vector<vector<double> > points_transformed_validation_new;
    for (int i = 0; i < num_points_validation; i++) {
        point p = points_validation[i];
        vector<double> transformed_point = transform_point(p);
        points_transformed_validation_new.push_back(transformed_point);
    }
    points_transformed_validation = points_transformed_validation_new;
}

vector<double> LinearRegression::transform_point(point p) {
    vector<double> transformed_point;
    if (dim >= 1)
        transformed_point.push_back(1);
    if (dim >= 2)
        transformed_point.push_back(p.x);
    if (dim >= 3)
        transformed_point.push_back(p.y);
    if (dim >= 4)
        transformed_point.push_back(pow(p.x, 2));
    if (dim >= 5)
        transformed_point.push_back(pow(p.y, 2));
    if (dim >= 6)
        transformed_point.push_back(p.x * p.y);
    if (dim >= 7)
        transformed_point.push_back(abs(p.x - p.y));
    if (dim >= 8)
        transformed_point.push_back(abs(p.x + p.y));
    return transformed_point;
}

void LinearRegression::make_matrices() {
    mat x_matrix_new;
    mat y_matrix_new;
    for (int i = 0; i < num_points; i++) {
        mat x_row(points_transformed[i]);
        x_row = x_row.t();
        x_matrix_new.insert_rows(i, x_row);

        vector<double> vec;
        vec.push_back(points[i].score);
        mat y_row(vec);
        y_matrix_new.insert_rows(i, y_row);
    }
    x_matrix = x_matrix_new;
    y_matrix = y_matrix_new;
}

void LinearRegression::make_matrix_out() {
    mat x_matrix_out_new;
    for (int i = 0; i < num_points_out; i++) {
        mat x_row(points_transformed_out[i]);
        x_row = x_row.t();
        x_matrix_out_new.insert_rows(i, x_row);
    }
    x_matrix_out = x_matrix_out_new;
}

void LinearRegression::make_matrix_validation() {
    mat x_matrix_validation_new;
    for (int i = 0; i < num_points_validation; i++) {
        mat x_row(points_transformed_validation[i]);
        x_row = x_row.t();
        x_matrix_validation_new.insert_rows(i, x_row);
    }
    x_matrix_validation = x_matrix_validation_new;
}

void LinearRegression::set_points(vector<point> val) {
    num_points = val.size();
    points = val;
    make_points_transformed();
    make_matrices();
}

void LinearRegression::set_points_out(vector<point> val) {
    points_out = val;
    num_points_out = points_out.size();
    make_points_transformed_out();
    make_matrix_out();
}

void LinearRegression::set_points_validation(vector<point> val) {
    points_validation = val;
    num_points_validation = points_validation.size();
    make_points_transformed_validation();
    make_matrix_validation();
}

mat LinearRegression::get_weight_matrix() {
    if (w_matrix_set == false) {
        w_matrix = pinv(x_matrix) * y_matrix;
    }
    return w_matrix;
}

mat LinearRegression::get_weight_matrix_reg() {
   if (w_matrix_reg_set == false) {
       mat I(dim, dim);
       double lambda = pow(10, k);
       w_matrix_reg = inv((x_matrix.t() * x_matrix + lambda * I.eye())) *
           (x_matrix.t() * y_matrix);
   }
   return w_matrix_reg;
}

double LinearRegression::get_e_classification_in(bool weight_decay) {
    mat w_matrix;
    if (weight_decay)
        w_matrix = get_weight_matrix_reg();
    else
        w_matrix = get_weight_matrix();
    double count = 0;
    for (int i = 0; i < num_points; i++) {
        rowvec row = x_matrix.row(i);
        double dot_product = dot(row, w_matrix);
        int sign_dot_product = sign(dot_product);
        if (sign_dot_product != points[i].score)
            count++;
    }
    return count / num_points;
}

double LinearRegression::get_e_classification_out(bool weight_decay) {
    mat w_matrix;
    if (weight_decay)
        w_matrix = get_weight_matrix_reg();
    else
        w_matrix = get_weight_matrix();
    double count = 0;
    for (int i = 0; i < num_points_out; i++) {
        rowvec row = x_matrix_out.row(i);
        double dot_product = dot(row, w_matrix);
        int sign_dot_product = sign(dot_product);
        if (sign_dot_product != points_out[i].score)
            count++;
    }
    return count / num_points_out;
}

double LinearRegression::get_e_classification_validation(bool weight_decay) {
    mat w_matrix;
    if (weight_decay)
        w_matrix = get_weight_matrix_reg();
    else
        w_matrix = get_weight_matrix();
    double count = 0;
    for (int i = 0; i < num_points_validation; i++) {
        rowvec row = x_matrix_validation.row(i);
        double dot_product = dot(row, w_matrix);
        int sign_dot_product = sign(dot_product);
        if (sign_dot_product != points_validation[i].score)
            count++;
    }
    return count / num_points_validation;
}
double LinearRegression::get_e_squared_validation(bool weight_decay) {
    mat w_matrix;
    if (weight_decay)
        w_matrix = get_weight_matrix_reg();
    else
        w_matrix = get_weight_matrix();
    double sum = 0;
    for (int i = 0; i < num_points_validation; i++) {
        rowvec row = x_matrix_validation.row(i);
        double dot_product = dot(row, w_matrix);
        double squared_error = pow(dot_product - points_validation[i].score, 2);
        sum += squared_error;
    }
    return sum / num_points_validation;
}

double LinearRegression::get_e_cross_validation(bool weight_decay) {
    vector<point> original_points = points;
    double sum_error = 0;
    for (int i = 0; i < original_points.size(); i++) {
        vector<point> current_points;
        vector<point> current_val_points;
        for (int j = 0; j < original_points.size(); j++) {
            if (j != i)
                current_points.push_back(original_points[j]);
            else
                current_val_points.push_back(original_points[j]);
        }
        set_points(current_points);
        set_points_validation(current_val_points);
        get_weight_matrix();
        double error = get_e_squared_validation(false);
        sum_error += error;
    }
    set_points(original_points);
    double cross_validation_error = sum_error / num_points;
    return cross_validation_error;
}

int LinearRegression::sign(double num) {
    if (num > 0) return 1;
    else return -1;
}

void LinearRegression::print_matrices() {
    x_matrix.print("x_matrix:");
    y_matrix.print("y_matrix:");
    x_matrix_validation.print("x_matrix_validation:");
    //x_matrix_out.print("x_matrix_out:");
    w_matrix.print("w_matrix:");
}


LinearRegression::~LinearRegression()
{
}
