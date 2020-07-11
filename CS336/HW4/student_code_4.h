///////////////////////////////////////////////////////////////////////////////
// You need to 
//    1. Read Problem 1 in homework assignment #4. 
//    2. Implement function GetStudentName.
//    3. Implement function FindMaxProfit.
//    4. Compile your code as explained in the PDF file.
//    5. Run the executable.
//    6. Test and debug your code.
//    7. Submit your code ("student_code_4.h") and results ("solution_4.dat") 
//       via Canvas.
///////////////////////////////////////////////////////////////////////////////

//required libraries
#include <string>
#include <vector> 

//you can include standard C++ libraries here
#include <queue>
#include <algorithm>

// This function should return your name.
// The name should match your name in Canvas

void GetStudentName(std::string& your_name)
{
   //replace the placeholders "Firstname" and "Lastname"
   //with you first name and last name 
   your_name.assign("Zhu Xu");
}

int FindMaxProfit (const std::vector<int>& north, const std::vector<int>& west)
{  

   if (north.size() == 1)
      return std::max(north[0], west[0]);
   else if (north.size() == 2)
      return std::max(north[0] + north[1], west[0] + west[1]);
   else {
      std::vector northRecord(north.size(), 0), westRecord(west.size(), 0);
      northRecord[0] = north[0];
      northRecord[1] = north[1] + north[0];
      westRecord[0] = west[0];
      westRecord[1] = west[1] + west[0];

      for (int i = 2; i != north.size(); ++i) {
         northRecord[i] = std::max(northRecord[i-1] + north[i], westRecord[i-2] + north[i]);
         westRecord[i] = std::max(westRecord[i-1] + west[i], northRecord[i-2] + west[i]);
      }

      return std::max(northRecord[north.size() - 1], westRecord[north.size() - 1]);
   }
}


