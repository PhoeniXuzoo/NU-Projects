///////////////////////////////////////////////////////////////////////////////
// You need to
//    1. Read the programming assignment in homework #3.
//    2. Implement function GetStudentName.
//    3. Implement function MinMatching.
//    4. Compile your code as explained in the PDF file.
//    5. Run the executable.
//    6. Test and debug your code.
//    7. Submit your code ("student_code_3.h") and results ("solution_3.dat")
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

int MinMatching(std::vector<int> red, std::vector<int> blue)
{
   int total = 0;
   std::sort(red.begin(), red.end());
   std::sort(blue.begin(), blue.end());

   for (int i = 0; i != red.size(); ++i) {
      total += abs(red[i] - blue[i]);
   }

   return total /* your answer */;
}
