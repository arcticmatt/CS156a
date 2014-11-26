from __future__ import division
from sklearn import svm
from cvxopt import matrix, solvers, spmatrix
import sys
import random
import numpy as np

class PlaAndSvm:
    def __init__(self, num_points):
        self.num_points = num_points
        self.in_points = self.generate_random_points(self.num_points)
        self.in_point_scores = []
        self.out_points = self.generate_random_points(1000)
        self.out_point_scores = []
        self.weight_vector = [0, 0, 0]
        self.line_point = []
        self.line_slope = 0
        self.iterations = 0
        self.generate_random_line()
        self.set_scores_for_points()
        self.num_support_vectors = 0

    def print_lists(self):
        for point in self.in_points:
            print 'point[0]: ', point[0]
            print 'point[1]: ', point[1]
            print 'point[2]: ', point[2]

    def generate_random_points(self, num_points):
        point_list = []
        for i in range (0, num_points):
            x = random.random() * 2 - 1
            y = random.random() * 2 - 1
            point = [0, x, y]
            point_list.append(point)
        return point_list

    def generate_random_line(self):
        x1 = random.random() * 2 - 1
        y1 = random.random() * 2 - 1
        x2 = random.random() * 2 - 1
        y2 = random.random() * 2 - 1
        slope = (y2 - y1) / (x2 - x1)
        self.line_point = [0, x1, y1]
        self.line_slope = slope

    def set_scores_for_points(self):
        for point in self.in_points:
            score = self.get_score_for_point(point)
            self.in_point_scores.append(score)
            point[0] = score
        for point in self.out_points:
            score = self.get_score_for_point(point)
            self.out_point_scores.append(score)
            point[0] = score

    def get_score_for_point(self, point):
        line_x = (point[2] - self.line_point[2]) / self.line_slope + self.line_point[1]
        if line_x > point[1]:
            return -1
        else:
            return 1

    def is_misclassified(self, point):
        dummy_point = []
        dummy_point.append(1)
        dummy_point.append(point[1])
        dummy_point.append(point[2])
        dot_sign = np.sign(np.dot(self.weight_vector, dummy_point))
        if dot_sign != point[0]:
            return True
        return False

    def count_misclassified(self, points_list):
        count = 0
        for point in points_list:
            if self.is_misclassified(point):
                count += 1
        return count

    def is_any_misclassified(self):
        count = self.count_misclassified(self.in_points)
        if count > 0:
            return True
        return False

    def adjust_weight_vector(self, point):
        self.iterations += 1
        self.weight_vector[0] += point[0]
        self.weight_vector[1] += point[0] * point[1]
        self.weight_vector[2] += point[0] * point[2]

    def run_pla_iteration(self):
        while (True):
            rand_picker = random.randint(0, self.num_points - 1)
            rand_point = self.in_points[rand_picker]
            if self.is_misclassified(rand_point):
                self.adjust_weight_vector(rand_point)
                break

    def run_pla(self):
        while self.is_any_misclassified():
            self.run_pla_iteration()

    def get_out_classification_error(self, isPla):
        if isPla:
            count = self.count_misclassified(self.out_points)
            return count / len(self.out_points)
        else:
            # GET SVM ERROR
            svm_points = []
            for point in self.in_points:
                svm_point = [point[1], point[2]]
                svm_points.append(svm_point)
            svm_scores = self.in_point_scores;
            if svm_scores.count(svm_scores[0]) == len(svm_scores):
                return -1
            clf = svm.SVC()
            clf.C = sys.maxint
            clf.fit(svm_points, svm_scores)
            self.num_support_vectors = len(clf.support_vectors_)
            print 'len support_vectors_ = ', len(clf.support_vectors_)
            print 'len n_support_ = ', clf.n_support_

    def get_svm_error(self, svm_weight_vector, b):
        svm_points = []
        svm_point_scores = []
        for point in self.out_points:
            svm_point = [point[1], point[2]]
            svm_points.append(svm_point)
            svm_point_scores.append(point[0])

        count = 0
        for i in range(0, len(svm_points)):
            dot_sign = np.sign(np.dot(svm_points[i], svm_weight_vector) + b)
            if dot_sign != svm_point_scores[i]:
                count += 1
        return count / len(svm_points)

    def run_svm(self):
        y = self.in_point_scores
        if y.count(y[0]) == len(y):
            return -1
        matrix_p_list = []
        matrix_q_list = []
        matrix_a_list = []
        for i in range(0, len(self.in_points)):
            list_item = []
            for j in range(0, len(self.in_points)):
                point1 = self.in_points[i]
                point2 = self.in_points[j]
                dummy_point1 = [point1[1], point1[2]]
                dummy_point2 = [point2[1], point2[2]]
                list_item.append(point1[0] * point2[0]
                        * np.dot(dummy_point1, dummy_point2))
            matrix_p_list.append(list_item)
            matrix_q_list.append(-1.0)
            matrix_a_list.append(self.in_point_scores[i] + 0.0)

        P = matrix(matrix_p_list)
        P = P.trans()
        q = matrix(-1.0, (1, len(self.in_points)))
        q = q.trans()
        A = matrix(matrix_a_list)
        A = A.trans()
        b = matrix(0.0)
        G = spmatrix(-1.0, range(len(self.in_points)), range(len(self.in_points)))
        h = matrix(0.0, (1, len(self.in_points)))
        h = h.trans()
        sol = solvers.qp(P, q, G, h, A, b)
        sol = sol['x']
        svm_weight_vector = [0, 0]
        num_support_vectors = 0
        sv_index = 0
        b = 0
        max = 0
        nn = []
        for i in range(0, len(sol)):
            if (sol[i] > 10 ** -5):
                num_support_vectors += 1
                mult = sol[i] * self.in_points[i][0]
                svm_weight_vector[0] += mult * self.in_points[i][1]
                svm_weight_vector[1] += mult * self.in_points[i][2]
                if sol[i] > max:
                    sv_index = i
                    max = sol[i]
                nn.append(sol[i])
        dummy_point = [self.in_points[sv_index][1], self.in_points[sv_index][2]]
        b = (1 - self.in_points[sv_index][0] * np.dot(svm_weight_vector,
            dummy_point)) / self.in_points[sv_index][0]
        error = self.get_svm_error(svm_weight_vector, b)
        self.num_support_vectors = num_support_vectors
        return error


if __name__ == '__main__':
    print 'Kshit is gay'
    num_points = 100
    num_runs = 1000
    sum_iterations = 0
    sum_e_out_pla = 0
    sum_e_out_svm = 0
    sum_support_vecs = 0
    num_svm_runs = 0
    num_svm_better = 0
    for i in range(0, num_runs):
        p = PlaAndSvm(num_points)
        p.run_pla()
        sum_iterations += p.iterations
        e_out_pla = p.get_out_classification_error(True)
        e_out_svm = p.run_svm()
        p.get_out_classification_error(False)
        if e_out_svm != -1:
            sum_e_out_pla += e_out_pla
            num_svm_runs += 1
            sum_support_vecs += p.num_support_vectors
            sum_e_out_svm += e_out_svm
            if e_out_svm < e_out_pla:
                num_svm_better += 1
                print('num_svm_better = ' + str(num_svm_better) + ' num_svm_runs = '
                + str(num_svm_runs))
    avg_iterations = sum_iterations / num_runs
    avg_e_out_pla = sum_e_out_pla / num_svm_runs
    avg_e_out_svm = sum_e_out_svm / num_svm_runs
    avg_support_vecs = sum_support_vecs / num_svm_runs
    frac_svm_better = num_svm_better / num_svm_runs
    print 'num points = ', num_points
    print 'average iterations = ', avg_iterations
    print 'average e out pla = ', avg_e_out_pla
    print 'average e out svm = ', avg_e_out_svm
    print 'num svm runs = ', num_svm_runs
    print 'num svm better = ', num_svm_better
    print 'frac svm better = ', frac_svm_better
    print 'avg num support vecs = ', avg_support_vecs
