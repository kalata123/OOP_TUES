package school.grading;

import school.people.Student;
import school.util.Config;

public final class GradeBook {
    public void addScore(Student s, int score) throws InvalidScoreException {
        if(score < 0 || score > Config.MAX_SCORE) {
            throw new InvalidScoreException("Invalid score");
        }

        int len = s.getScores().length;
        s.getScores()[len-1] = score;
    }

//    int... scores introduces variable arguments...
//    example usage:
//    class Geeks {
//
//        // A method that takes variable number of integer arguments.
//        static void fun(int... a)
//        {
//            System.out.println("Number of arguments: " + a.length);
//
//            // using for each loop to display contents of a
//            for (int i : a)
//                System.out.print(i + " ");
//            System.out.println();
//        }
//
//        // Driver code
//        public static void main(String args[])
//        {
//            // Calling the varargs method with one parameter
//            fun(100);
//
//            // four parameters
//            fun(1, 2, 3, 4);
//
//            // no parameter
//            fun();
//        }
//    }
    public void addScore(Student s, int... scores) throws InvalidScoreException {
        for ( int i : scores ) {
            if(i < 0 || i > Config.MAX_SCORE) {
                throw new InvalidScoreException("Invalid score");
            }

            int len = s.getScores().length;
            s.getScores()[len-1] = i;
        }

    }
}
