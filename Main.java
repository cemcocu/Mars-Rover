import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws CustomException {
        char[] directions={'E','S','W','N'};
        int[] squareSize =new int[2];
        int[] rover1=new int[3];
        int[] rover2=new int[3];
        List<Character> rover1Instruction = new ArrayList<>();
        List<Character> rover2Instruction = new ArrayList<>();
        Scanner userinput = new Scanner(System.in);
        System.out.println("Enter square size in this type : \"number space number\" etc.(5 5) : \n");
        validateAndAssignSquareSize(userinput.nextLine(),squareSize);
        System.out.println("Enter first rover position : \"number space number space direction\" etc.(1 2 N) : \n");
        validateAndAssignRoverLocation(squareSize,userinput.nextLine(),rover1,directions);
        System.out.println("Enter first rover instructions : \"Use just R,M,L\" etc.(RMRMLMMLMRMM) : \n");
        validateAndAssignRoverInstruction(userinput.nextLine(),rover1Instruction);
        System.out.println("Enter second rover position : \"number space number space direction\" etc.(1 2 N) : \n");
        validateAndAssignRoverLocation(squareSize,userinput.nextLine(),rover2,directions);
        System.out.println("Enter second rover instructions : \"Use just R,M,L\" etc.(RMRMLMMLMRMM) : \n");
        validateAndAssignRoverInstruction(userinput.nextLine(),rover2Instruction);

        //JVM process the code line by line so rovers will move on the square sequentally(expected behaviour).Not necessary to syncronize them for concurrent
        roverRun(squareSize,rover1,rover1Instruction,directions);
        roverRun(squareSize,rover2,rover2Instruction,directions);

    }
    private static void validateAndAssignSquareSize(String squareSizeInput, int[] squareSize)
    {
        if (Character.isDigit(squareSizeInput.charAt(0)) && squareSizeInput.charAt(1) == ' ' &&
                Character.isDigit(squareSizeInput.charAt(2))&&squareSizeInput.length()==3)
        {
            squareSize[0] = Character.getNumericValue(squareSizeInput.toCharArray()[0]);
            squareSize[1] = Character.getNumericValue(squareSizeInput.toCharArray()[2]);
        }
        else
        {
            System.out.print("wrong square size format\n");
        }
    }
    private static void validateAndAssignRoverLocation(int[] squareSize, String roverLocationInput, int[] rover,char[] directions)
    {
        if (Character.isDigit(roverLocationInput.charAt(0)) && roverLocationInput.charAt(1) == ' ' &&
                Character.isDigit(roverLocationInput.charAt(2))&&roverLocationInput.charAt(3)==' ' &&
                String.valueOf(directions).contains((String.valueOf(roverLocationInput.charAt(4)))))
        {
            rover[0] = Character.getNumericValue(roverLocationInput.toCharArray()[0]);
            rover[1] = Character.getNumericValue(roverLocationInput.toCharArray()[2]);
            rover[2] = roverLocationInput.toCharArray()[4];
        }
        else
        {
            System.out.print("wrong location format\n");
        }
        if (rover[0]>squareSize[0] || rover[1]>squareSize[1])
        {
            System.out.print("rover is not in the square\n");
        }
    }
    private static void validateAndAssignRoverInstruction(String roverInstructionInput,List<Character> roverInstruction)
    {
        char[] instructions={'R','M','L'};

        for (int i = 0; i < roverInstructionInput.length(); i++) {
            roverInstruction.add(roverInstructionInput.charAt(i));
        }
        roverInstruction.parallelStream().forEach(elem->{
            if (!String.valueOf(instructions).contains(String.valueOf(elem)))
            {
                try {
                    throw new CustomException("wrong instruction\n");
                } catch (CustomException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private static void roverRun(int[] squareSize, int[] rover, List<Character> rover1Instruction, char[] directions) throws CustomException
    {
        for (int i=0;i<rover1Instruction.size();i++)
        {
            switch (rover1Instruction.get(i))
            {
                case 'L':
                    lBehaviour(rover,directions);
                    break;
                case 'M':
                    mBehaviour(rover,squareSize);
                    break;
                case 'R':
                    rBehaviour(rover,directions);
                    break;
                default:
                    System.out.println("no match");
            }
        }
        System.out.print(new StringBuilder().append(rover[0]).append(" ").append(rover[1]).append(" ").append((char)rover[2]).append("\n").toString());
    }

    // rBehaviour and lBehaviour together are a simple implementation of circular linked list for directions

    private static void rBehaviour(int[] rover,char[] directions)
    {
        int i =new String(directions).indexOf(((char)rover[2]));

        if (i==3)
        {
            rover[2]=directions[0];
        }
        else
        {
            rover[2] = directions[i + 1];
        }
    }
    private static void lBehaviour(int[] rover,char[] directions)
    {
        int i =new String(directions).indexOf(((char)rover[2]));

        if (i==0)
        {
            rover[2]=directions[3];
        }
        else
        {
            rover[2] = directions[i - 1];
        }
    }
    private static void mBehaviour(int[] rover,int[] squareSize) throws CustomException
    {
        char c = (char)rover[2];

        if (c=='N')
        {
            if (rover[1]==squareSize[1])
            {
               throw new CustomException("out of square");
            }
            else
            {
                rover[1] = rover[1] + 1;
            }
        }
        if (c=='S' )
        {
            if (rover[1]==0)
            {
                throw new CustomException("out of square");
            }
            else
            {
                rover[1] = rover[1] - 1;
            }
        }
        if (c=='W')
        {
            if (rover[0]==0)
            {
                throw new CustomException("out of square");
            }
            else
            {
                rover[0] = rover[0] - 1;
            }
        }
        if (c=='E')
        {
            if (rover[0]==squareSize[0])
            {
                throw new CustomException("out of square");
            }
            else
            {
                rover[0] = rover[0] + 1;
            }
        }
    }
}
class CustomException extends Exception{
            public CustomException(String errorMessage)
            {
                super(errorMessage);
            }
        }