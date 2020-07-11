///////////////////////////////////////////////////////////////////////////////
// You need to
//    1. Read the programming assignment in homework #1.
//    2. Implement function GetStudentName.
//    3. Implement function CountNUOccurrences.
//    4. Compile your code as explained in the PDF file.
//    5. Run the executable.
//    6. Test and debug your code.
//    7. Submit your code ("student_code_1.h") and results ("solution_1.dat")
//       via Canvas.
///////////////////////////////////////////////////////////////////////////////

//required libraries
#include <string>
#include <vector>

//you can include standard C++ libraries here
#include <queue>

// This function should return your name.
// The name should match your name in Canvas

void GetStudentName(std::string& your_name)
{
   //replace the placeholders "Firstname" and "Lastname"
   //with you first name and last name
   your_name.assign("Zhu Xu");
}

int CountNUOccurrences(const std::string&  message)
{
   /*
    * Finite state machine, there are two states, 0 and 1.
    * The machine begins with the state 0. 
    * 
    * state 0:
    * If the input is 'N', the state will transfer from 0 to 1. If not, the state will stay 0.
    * 
    * state 1:
    * If the input is 'N', the state will not change.
    * If the input is 'U', the state will change to 0. ++count.
    * If the input is other character, the state will change to 0.
    */
   int count = 0, st = 0;

   for (const auto &ch : message) {
      switch (st)
      {
      case 0:
         if (ch == 'N') st = 1;
         break;
      case 1:
         if (ch == 'U') {
            st = 0;
            ++count;
         }
         else if (ch != 'N') st = 0;
      default:
         break;
      }
   }
   
   return count;
}
