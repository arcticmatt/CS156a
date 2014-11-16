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
        LinearRegression(vector<point> input);
        ~LinearRegression();

        mat get_weight_matrix();
        mat get_weight_matrix_reg();
        double get_e_classification_in(bool weight_decay);
        double get_e_classification_out(bool weight_decay);
        void set_points_out(vector<point> val);

        int k = -3;    // For the weight decay
        int num_points;
        int num_points_out;
    private:
        void make_points_transformed();
        void make_points_transformed_out();
        vector<double> transform_point(point p);
        void make_matrices();
        void make_matrix_out();
        int sign(double num);

        vector<point> points;
        vector<vector<double> > points_transformed;
        vector<point> points_out;
        vector<vector<double> > points_transformed_out;
        mat x_matrix;
        mat x_matrix_out;
        mat y_matrix;
        mat w_matrix;
        mat w_matrix_reg;
        bool w_matrix_set = false;
        bool w_matrix_reg_set = false;
        int weight_vector_length = 8;
};
