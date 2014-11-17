#include <vector>
#include <armadillo>

using namespace std;
using namespace arma;

struct point
{
    double x;
    double y;
    int score;
};

class LinearRegression
{
    public:
        LinearRegression();
        LinearRegression(vector<point> input, int dimension);
        ~LinearRegression();

        mat get_weight_matrix();
        mat get_weight_matrix_reg();
        double get_e_classification_in(bool weight_decay);
        double get_e_classification_out(bool weight_decay);
        double get_e_classification_validation(bool weight_decay);
        double get_e_squared_validation(bool weight_decay);
        double get_e_cross_validation(bool weight_decay);
        void set_points(vector<point> val);
        void set_points_out(vector<point> val);
        void set_points_validation(vector<point> val);
        void print_matrices();

        int k = -3;    // For the weight decay
        int num_points;
        int num_points_out;
        int num_points_validation;
    private:
        void make_points_transformed();
        void make_points_transformed_out();
        void make_points_transformed_validation();
        vector<double> transform_point(point p);
        void make_matrices();
        void make_matrix_out();
        void make_matrix_validation();
        int sign(double num);

        vector<point> points;
        vector<vector<double> > points_transformed;
        vector<point> points_out;
        vector<vector<double> > points_transformed_out;
        vector<point> points_validation;
        vector<vector<double> > points_transformed_validation;
        mat x_matrix;
        mat x_matrix_out;
        mat x_matrix_validation;
        mat y_matrix;
        mat w_matrix;
        mat w_matrix_reg;
        bool w_matrix_set = false;
        bool w_matrix_reg_set = false;
        int dim;
};
