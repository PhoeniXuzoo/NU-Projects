///////////////////////////////////////////////////////////////////////////////
// You need to
//    1. Read the programming assignment in homework #2.
//    2. Implement function GetStudentName.
//    3. Implement function FindOptimalSchedule.
//    4. Compile your code as explained in the PDF file.
//    5. Run the executable.
//    6. Test and debug your code.
//    7. Submit your code ("student_code_2.h") and results ("solution_2.dat")
//       via Canvas.
///////////////////////////////////////////////////////////////////////////////

//required libraries
#include <string>
#include <vector>

//you can include standard C++ libraries here
#include <algorithm>
#include <utility>

// This function should return your name.
// The name should match your name in Canvas
void GetStudentName(std::string& your_name)
{
   //replace the placeholders "Firstname" and "Lastname"
   //with you first name and last name
   your_name.assign("Zhu Xu");
}

// Data structure for jobs:
//    start - is the start time of the job;
//    finish - is the finish time of the job.

// You can assume that start >= 0, finish >= 0, and finish > start.

struct Job
{
   int start;
   int finish;
};

// Hint: To sort an array, you can use std::sort.
// See
//    https://en.cppreference.com/w/cpp/algorithm/sort
// or 
//    http://www.cplusplus.com/reference/algorithm/sort/

//You can use one of the following function signatures:
//   1.  int FindOptimalSchedule(const std::vector<Job>& highPriorityJobs, const std::vector<Job>& lowPriorityJobs)
//   2.  int FindOptimalSchedule(std::vector<Job> highPriorityJobs, std::vector<Job> lowPriorityJobs)

int FindOptimalSchedule(std::vector<Job> highPriorityJobs, std::vector<Job> lowPriorityJobs)
{
   std::sort(highPriorityJobs.begin(), highPriorityJobs.end(), [] (const auto &a, const auto &b) { return a.start < b.start; });
   std::sort(lowPriorityJobs.begin(), lowPriorityJobs.end(), [] (const auto &a, const auto &b) { return a.finish < b.finish; });

   int result = 0;
   std::vector<std::pair<int, int>> arrangeRegion;
   
   if (lowPriorityJobs.empty()) return 0;

   if (highPriorityJobs.empty()) 
      arrangeRegion.emplace_back(std::make_pair(0, INT_MAX));
   else {
      arrangeRegion.emplace_back(std::make_pair(0, highPriorityJobs[0].start));
      for (int i = 0; i != highPriorityJobs.size() - 1; ++i) 
         arrangeRegion.emplace_back(std::make_pair(highPriorityJobs[i].finish, highPriorityJobs[i + 1].start));
      arrangeRegion.emplace_back(std::make_pair(highPriorityJobs[highPriorityJobs.size() - 1].finish, INT_MAX));
   }

   int arrangeRegionCursor = 0, lowPriorityJobsCursor = 0, previousFinishTime = 0;
   std::vector<std::vector<Job>> lowPriorityJobsSplit;

   while (arrangeRegionCursor < arrangeRegion.size() && lowPriorityJobsCursor < lowPriorityJobs.size()) {
      if (lowPriorityJobs[lowPriorityJobsCursor].finish > arrangeRegion[arrangeRegionCursor].second) {
         ++arrangeRegionCursor;
         continue;
      }

      if (lowPriorityJobs[lowPriorityJobsCursor].finish <= arrangeRegion[arrangeRegionCursor].second 
         && lowPriorityJobs[lowPriorityJobsCursor].start >= arrangeRegion[arrangeRegionCursor].first 
         && lowPriorityJobs[lowPriorityJobsCursor].start >= previousFinishTime) {
            ++result;
            previousFinishTime = lowPriorityJobs[lowPriorityJobsCursor].finish;
         }
      ++lowPriorityJobsCursor;
   }

   return result;

}
