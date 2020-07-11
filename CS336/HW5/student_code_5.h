///////////////////////////////////////////////////////////////////////////////
// You need to
//    1. Read the programming assignment in homework #5.
//    2. Implement function GetStudentName.
//    3. Implement function MaxPurpleColoring.
//    4. Compile your code as explained in the PDF file.
//    5. Run the executable.
//    6. Test and debug your code.
//    7. Submit your code ("student_code_5.h") and results ("solution_5.dat")
//       via Canvas.
///////////////////////////////////////////////////////////////////////////////

//required libraries
#include <string>
#include <vector>

//you can include standard C++ libraries here
#include <algorithm>

// This function should return your name.
// The name should match your name in Canvas

void GetStudentName(std::string& your_name)
{
   //replace the placeholders "Firstname" and "Lastname"
   //with you first name and last name
   your_name.assign("Zhu Xu");
}

int MaxRedPurpleTree(const std::vector<int>& nodeWeights)
{
   if (nodeWeights.size() == 0) return 0;
   else if (nodeWeights.size() == 1) return nodeWeights[0];
   else if (nodeWeights.size() == 2) return nodeWeights[0] + nodeWeights[1];

   int i = nodeWeights.size() - 1, n = (nodeWeights.size() % 2) ? nodeWeights.size() : nodeWeights.size() + 1;
   std::vector<std::vector<int>> dp(n, std::vector<int>(2, 0));

   for (; (2*i + 1) >= nodeWeights.size(); --i) {
      dp[i][0] = 0;
      dp[i][1] = nodeWeights[i];
   }

   for (; i >= 0; --i) {
      dp[i][0] = std::max({dp[2 * i + 1][0] + dp[2 * i + 2][0], 
                           dp[2 * i + 1][1] + dp[2 * i + 2][0],
                           dp[2 * i + 1][0] + dp[2 * i + 2][1], 
                           dp[2 * i + 1][1] + dp[2 * i + 2][1]});
      dp[i][1] = std::max({dp[2 * i + 1][0] + dp[2 * i + 2][1],
                           dp[2 * i + 1][1] + dp[2 * i + 2][0]}) + nodeWeights[i];
   }
   
   return std::max(dp[0][0], dp[0][1]); // your answer
}
