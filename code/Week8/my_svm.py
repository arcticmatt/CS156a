from __future__ import division
from svm import *
from svmutil import *
from collections import Counter
import sys
import random

class MySvm:
    def __init__(self, file_train, file_test):
        self.training_labels = self.load_labels(file_train)
        self.training_data = self.load_data(file_train)
        self.training_data_curr = self.training_data
        self.scores_tr = []

        self.test_labels = self.load_labels(file_test)
        self.test_data = self.load_data(file_test)
        self.test_data_curr = self.test_data
        self.scores_test = []

        # Lists of lists
        self.cross_data_curr_list = []
        self.scores_cross_list = []
        self.scores_tr_list = []
        self.training_data_curr_list = []

        self.model = None
        self.model_list = []
        self.num_support_vectors = 0

    def load_labels(self, file_name):
        file_obj = open(file_name)
        labels = []
        for line in file_obj:
            labels.append(float(line.split()[0]))
        return labels

    def load_data(self, file_name):
        file_obj = open(file_name)
        data = []
        for line in file_obj:
            item = []
            line_split = line.split()
            for i in range(1, len(line_split)):
                item.append(float(line_split[i]))
            data.append(item)
        return data

    def one_versus_all(self, num_one, kernel_type, error_const, poly_degree,
            is_cross, is_repeat = False):
        param_str = '-t %d -r 1 -g 1 -c %f -d %d' %(kernel_type, error_const, poly_degree)
        if not is_repeat:
            self.scores_tr, self.training_data_curr = self.get_one_versus_all_lists(
                    num_one, self.training_labels, self.training_data)
            self.scores_test, self.test_data_curr = self.get_one_versus_all_lists(
                    num_one, self.test_labels, self.test_data)
            if is_cross:
                self.scores_tr_list, self.training_data_curr_list, self.scores_cross_list, self.cross_data_curr_list = self.get_cross_val_lists()

        if not is_cross:
            self.model = self.get_model(param_str, self.scores_tr, self.training_data_curr)
            self.num_support_vectors = len(model.get_SV())
        else:
            self.model_list = []
            sum_num_support_vectors = 0
            for i in range(0, len(self.scores_tr_list)):
                self.model_list.append(self.get_model(param_str, self.scores_tr_list[i],
                    self.training_data_curr_list[i]))
                sum_num_support_vectors += len(self.model_list[i].get_SV())
            self.num_support_vectors = sum_num_support_vectors / len(self.scores_tr_list)

    def one_versus_one(self, num_one, num_other, kernel_type, error_const,
            poly_degree, is_cross, is_repeat = False):
        param_str = '-t %d -r 1 -g 1 -c %f -d %d' %(kernel_type, error_const, poly_degree)
        if not is_repeat:
            self.scores_tr, self.training_data_curr = self.get_one_versus_one_lists(
                    num_one, num_other, self.training_labels, self.training_data)
            self.scores_test, self.test_data_curr = self.get_one_versus_one_lists(
                    num_one, num_other, self.test_labels, self.test_data)
            if is_cross:
                self.scores_tr_list, self.training_data_curr_list, self.scores_cross_list, self.cross_data_curr_list = self.get_cross_val_lists()

        if not is_cross:
            self.model = self.get_model(param_str, self.scores_tr, self.training_data_curr)
            self.num_support_vectors = len(model.get_SV())
        else:
            self.model_list = []
            sum_num_support_vectors = 0
            for i in range(0, len(self.scores_tr_list)):
                self.model_list.append(self.get_model(param_str, self.scores_tr_list[i],
                    self.training_data_curr_list[i]))
                sum_num_support_vectors += len(self.model_list[i].get_SV())
            self.num_support_vectors = sum_num_support_vectors / len(self.scores_tr_list)

    def get_model(self, param_str, scores_list, training_list):
        prob = svm_problem(scores_list, training_list)
        param = svm_parameter(param_str)
        model = svm_train(prob, param)
        return model

    def get_one_versus_all_lists(self, num_one, labels, data_items):
        scores = []
        for label in labels:
            if label == num_one:
                scores.append(1)
            else:
                scores.append(-1)
        return (scores, data_items)

    def get_one_versus_one_lists(self, num_one, num_other, labels, data_items):
        scores = []
        data_curr = []
        for i in range(0, len(labels)):
            label = labels[i]
            item = data_items[i]
            if label == num_one:
                scores.append(1)
                data_curr.append(item)
            elif label == num_other:
                scores.append(-1)
                data_curr.append(item)
        return (scores, data_curr)

    def get_cross_val_lists(self):
        z_list = list(zip(self.scores_tr, self.training_data_curr))
        random.shuffle(z_list)
        shuffled_scores_tr, shuffled_training_data_curr = zip(*z_list)
        index = int(len(self.scores_tr) / 10)

        scores_tr_list, training_data_curr_list, scores_cross_list, cross_data_curr_list = [], [], [], []
        start = 0
        end = index
        while end < len(self.scores_tr):
            # Last cross validation group may be slightly bigger
            if end + index >= len(self.scores_tr):
                end = len(self.scores_tr)
            scores_cross_list.append(list(shuffled_scores_tr[start:end]))
            cross_data_curr_list.append(list(shuffled_training_data_curr[start:end]))
            scores_tr_list.append(list(shuffled_scores_tr[0:start]) + list(shuffled_scores_tr[end:]))
            training_data_curr_list.append(list(shuffled_training_data_curr[0:start]) + list(shuffled_training_data_curr[end:]))
            start += index
            end += index
        return (scores_tr_list, training_data_curr_list, scores_cross_list, cross_data_curr_list)

    def get_error_in(self):
        p_labels, p_acc, p_vals = svm_predict(self.scores_tr,
                self.training_data_curr, self.model)
        return p_acc[0]

    def get_error_out(self):
        p_labels, p_acc, p_vals = svm_predict(self.scores_test,
                self.test_data_curr, self.model)
        return p_acc[0]

    def get_error_cv(self):
        sum_acc = 0
        for i in range(0, len(self.scores_cross_list)):
            p_labels, p_acc, p_vals = svm_predict(self.scores_cross_list[i],
                    self.cross_data_curr_list[i], self.model_list[i])
            sum_acc += p_acc[0]
        return sum_acc / len(self.scores_cross_list)

if __name__ == '__main__':
    prob1_3 = False
    prob5 = False
    prob6 = False
    prob7_8 = True
    prob9_10 = False
    print 'Kshit is gay'
    my_svm = MySvm('features.train', 'features.test')

    if prob1_3:
        for i in range(1, 11, 2):
            print 'i = ', i
            my_svm.one_versus_all(i, 1, .01, 2, False)
            p_acc = my_svm.get_error_in()
            print 'Num support vectors = ', my_svm.num_support_vectors
            print '\n\n'

    if prob5:
        print 'Q = ', 2
        for c in [.0001, .001, .01, .1, 1]:
            print 'C = ', c
            my_svm.one_versus_one(1, 5, 1, c, 2, False)
            print 'E in'
            my_svm.get_error_in()
            print 'E out'
            my_svm.get_error_out()
            print 'Num support vectors = ', my_svm.num_support_vectors
            print '\n'
        print '\n\n\n'

    if prob6:
        print 'Q = ', 5
        for c in [.0001, .001, .01, .1, 1]:
            print 'C = ', c
            my_svm.one_versus_one(1, 5, 1, c, 5, False)
            print 'E in'
            my_svm.get_error_in()
            print 'E out'
            my_svm.get_error_out()
            print 'Num support vectors = ', my_svm.num_support_vectors
            print '\n'

    if prob7_8:
        c_list = []
        e_dict = {.0001 : [], .001 : [], .01 : [], .1 : [], 1 : []}
        for i in range(0, 100):
            min_error = sys.maxint
            c_val = 1
            repeat = False
            for c in [.0001, .001, .01, .1, 1]:
                my_svm.one_versus_one(1, 5, 1, c, 2, True, repeat)
                repeat = True
                # Get error, NOT accurary
                error = (100 - my_svm.get_error_cv()) / 100
                e_dict[c].append(error)
                if error < min_error:
                    min_error = error
                    c_val = c
            c_list.append(c_val)
        c_data = Counter(c_list)
        print 'Counts = ', c_data.most_common()
        print 'Mode = ', c_data.most_common(1)
        for key, val in e_dict.iteritems():
            new_val = []
            val_max = max(val)
            val_min = min(val)
            val_mean = sum(val) / len(val)
            new_val = [val_mean, val_max, val_min]
            e_dict[key] = new_val
        print e_dict

    if prob9_10:
        min_error_in = sys.maxint
        min_error_out = sys.maxint
        c_val_in = 0
        c_val_out = 0
        for c in [.01, 1, 100, 10000, 1000000]:
            my_svm.one_versus_one(1, 5, 2, c, 2, False, False)
            error_in = (100 - my_svm.get_error_in()) / 100
            if error_in < min_error_in:
                min_error_in = error_in
                c_val_in = c
            error_out = (100 - my_svm.get_error_out()) / 100
            if error_out < min_error_out:
                min_error_out = error_out
                c_val_out = c
        print 'Min error in = ', min_error_in
        print 'C value in = ', c_val_in
        print 'Min error out = ', min_error_out
        print 'C value out = ', c_val_out
