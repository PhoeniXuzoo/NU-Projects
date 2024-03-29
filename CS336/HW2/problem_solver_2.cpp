////////////////////////////////////////////////////////////////////////////
// DO NOT EDIT THIS FILE
//

#include <string>
#include <vector>
#include "test_framework.h"
#include "student_code_2.h"

const char* inputFilename = "problem_set_2.in";
const char* outputFilename = "solution_2.dat";

const char* strNotice = "##################################\n"
                        "# Do not edit this file!\n"
                        "##################################\n";

constexpr int problem_set_id = 2;

struct ProblemN2 : public TestFramework::BasicProblem
{
   std::vector<int> left_high_priority;
   std::vector<int> right_high_priority;
   std::vector<int> left_low_priority;
   std::vector<int> right_low_priority;
};

void InitJobVector(/* out */ std::vector<Job>& jobs, const std::vector<int>& leftEndpoints, const std::vector<int>& rightEndpoints)
{
   TestFramework::ExitIfConditionFails(leftEndpoints.size() == rightEndpoints.size(), "The number of left endpoints does not math with the number of right endpoints.");
   
   size_t nSize = leftEndpoints.size();
   jobs.resize(nSize);

   for (size_t i = 0; i < nSize; ++i)
   {
      jobs[i].start = leftEndpoints[i];
      jobs[i].finish = rightEndpoints[i];
   }

   //verify that the set of job is valid
   for (Job j: jobs)
   {
      using namespace TestFramework;
      ExitIfConditionFails(j.start >= 0, "Start time must be non-negative.");
      ExitIfConditionFails(j.finish >= 0, "Finish time must be non-negative.");
      ExitIfConditionFails(j.start <= j.finish, "Start time must be after finish time.");
   }

}

int FindOptimalSchedule(const std::vector<int>& left_high_priority, const std::vector<int>& right_high_priority,
                        const std::vector<int>& left_low_priority, const std::vector<int>& right_low_priority)
{
   std::vector<Job> highPriorityJobs;
   std::vector<Job> lowPriorityJobs;

   InitJobVector (highPriorityJobs, left_high_priority, right_high_priority);
   InitJobVector (lowPriorityJobs, left_low_priority, right_low_priority);

   return FindOptimalSchedule(highPriorityJobs, lowPriorityJobs);
}

int main(int argc, char *argv[])
{
   using namespace TestFramework;

   static_assert (GetTestFrameworkVersion () == 6, "TestFramework version 6 is required. Please, update test_framework.h.");
   ExitIfConditionFails((argc == 1) || (argc == 2), "This program takes at most one parameter.");

   std::cout << std::endl << "Problem set #" << problem_set_id << ". ";

   ProblemSetHeader header;
   RecordAdapter<ProblemSetHeader> psAdapter(header);
   AddDefaultProblemSetColumns(psAdapter);

   std::vector<ProblemN2> problems;
   TableAdapter<ProblemN2> prAdapter(problems);
   AddDefaultProblemColumns(prAdapter);

   AddColumn<ProblemN2>(prAdapter, "left_high_priority", &ProblemN2::left_high_priority);
   AddColumn<ProblemN2>(prAdapter, "right_high_priority", &ProblemN2::right_high_priority);
   AddColumn<ProblemN2>(prAdapter, "left_low_priority", &ProblemN2::left_low_priority);
   AddColumn<ProblemN2>(prAdapter, "right_low_priority", &ProblemN2::right_low_priority);

   BasicYamlParser parser(dynamic_cast<ITable*>(&psAdapter), dynamic_cast<ITable*>(&prAdapter));
   parser.ParseFile(inputFilename, true);

   GetStudentName(header.student_name);

   PreprocessProblemSet(problem_set_id, problems, header);

   bool isDebugMode = (argc == 2);

   if (!isDebugMode)
   {
      for (int i = 0; i < header.problem_count; i++)
      {
         ProblemN2& theProblem = problems[i];
         theProblem.student_answer = FindOptimalSchedule(theProblem.left_high_priority, theProblem.right_high_priority, theProblem.left_low_priority, theProblem.right_low_priority);
      }

      ProcessResults(problems, header);

      TableAdapter<ProblemN2> prOutAdapter(problems);
      AddDefaultProblemColumnsForOutput(prOutAdapter);

      WriteTableToFile(outputFilename, &prOutAdapter, &psAdapter, false, strNotice);
      std::cout << "Don't forget to submit your source code and file '" << outputFilename << "' via Canvas.";
      std::cout << std::endl << std::endl;
   }
   else
   {
      int problemToSolve = atoi(argv[1]);

      ExitIfConditionFails(problemToSolve != 0,
         "Invalid parameter. Please, specify the number of the problem you want to solve. The number of the first problem is 1.");

      std::cout << "Solving problem #" << problemToSolve << "." << std::endl;

      ExitIfConditionFails(problemToSolve <= header.problem_count, "The problem you specified does not exist.");
      ProblemN2& theProblem = problems[problemToSolve - 1];
      theProblem.student_answer = FindOptimalSchedule(theProblem.left_high_priority, theProblem.right_high_priority, theProblem.left_low_priority, theProblem.right_low_priority);

      if (problemToSolve <= header.test_problems)
      {
            std::cout << "Correct answer: " << theProblem.correct_answer << "." << std::endl;
      }

      std::cout << "Your answer: " << theProblem.student_answer << "." << std::endl;
      std::cout << std::endl;
   }

   return 0;
}
