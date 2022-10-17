import java.util.InputMismatchException;
import java.util.Scanner;

public class Checkers {
    Pieces[][] board = new Pieces[8][8];
    Pieces[][] current = new Pieces[2][12];
    int black = 12;
    int white = 12;
    int count = 0;
    Pieces[] b = new Pieces[12];
    Pieces[] k = new Pieces[4]; // pieces that can be killed
    int kValue = 0; // number that can be killed
    Scanner uggs;

    public static void main(String[] args)
    {
        Scanner primary = new Scanner(System.in);
        Checkers checkers = new Checkers(primary);
        int x = 1;
        Pieces test;
        boolean ifKing = true;
        boolean regular = true;
        checkers.printBoard();
        while (checkers.white != 0 && checkers.black != 0) // both myst be true
        {
            System.out.println(x == 1 ? "Black" : "White");
            checkers.openSpots(x);
            if(checkers.count != 0)
            {
                checkers.pickMethod();
            }
            else if (checkers.count == 0)
            {
                while (true)
                {
                    System.out.println("Enter the number of the piece you are moving");
                    int pieceToMove = checkers.numberTest();
                    test = checkers.available(pieceToMove, x);
                    if (test == null)
                    {
                        System.out.println("please select a different piece");
                        continue;
                    }
                    ifKing = true;
                    if (test.isKing())
                    {
                        System.out.println("This is a King. Enter U to move upward or D to move downward (Use Capital)");
                        ifKing = primary.nextLine().equals("U"); //U is true, D is false
                    }
                    System.out.println("Enter R to move Right or L to move Left (Use capital)");
                    regular = primary.nextLine().equals("R"); // R is true, L is false
                    if (checkers.turn(test, ifKing, regular))
                    {
                        break;
                    }
                }
            }
            x = (x + 1) % 2;
        }
        if (checkers.black == 0)
        {
            System.out.println("White Player wins");
        }
        else
        {
            System.out.println("Black Player wins");
        }

    }

    public Checkers(Scanner uggs)
    {
        this.uggs = uggs;
        int mod = 2;
        int i = 0;
        int f = 3;
        int j = 8; // length/width
        for (int x = 0; x < f; x++)
        {
            for (int y = 0; y < j; y++)
            {
                double xx = x % mod;
                double yy = y % mod;
                if (xx != yy) { // try mod 2
                    Pieces first = new Pieces(x, y, false, i);
                    current[0][i] = first;
                    board[x][y] = first;
                    i++;
                }
            }
        }
        i = 0;
        int l = 7;
        int e = f + 1;
        for (int x = l; x > e; x--)
        {
            for (int y = 0; y < j; y++)
            {
                double xx = x %mod;
                double yy = y % mod;
                if (xx != yy) {
                    Pieces first = new Pieces(x, y, true, i);
                    current[1][i] = first;
                    board[x][y] = first;
                    i++;
                }
            }
        }
    }

    private void printBoard()
    {
        int j = 8;
        String boardLine = "|---|---|---|---|---|---|---|---|";
        String block = "|";
        for (int i = 0; i < j; i++) {
            System.out.println(boardLine);
            System.out.print(block);
            for (int n = 0; n < j; n++)
            {
                if (board[i][n] != null)
                {
                    System.out.print(board[i][n].getSymbol() + block);
                } else {
                    System.out.print("   " + block);
                }
            }
            System.out.println(); // try println
        }
        System.out.println(boardLine);
    }

    public Pieces available(int test, int s)
    {
        if (test >= 0 && test < 12)
        {
            if (current[s][test].isOnboard())
            {
                return current[s][test];
            }
        }
        return null;
    }

    public boolean touching (int a, int b)
    {
        int min = 0;
        int max = 8;
        boolean touch = (a >= min && a < max && b >= min && b < max);
        return touch;
    }

    public Pieces canJump(Pieces test, int z, int u)
    {
        int number = 1;
        int x = test.getX();
        int y = test.getY();
        boolean color = test.getColor();
        x += (z) * (color ? -number : number);
        y += u;
        if (touching(x,y) && (board[x][y] != null && (board[x][y].getColor() != color)))
        {
            // has to be in variable form, do not try with .get
            int xx = x + (z * (color ? -number : number));
            int yy = y + u;
            if (touching(xx, yy) && board[xx][yy] == null)
            {
                test.setA(true);
                return board[x][y];
            }
        }
        return null;
    }

    public boolean turn(Pieces test, boolean f, boolean u)
    {
        int number = 1;
        int x = test.getX();
        int y = test.getY();
        boolean color = test.getColor();
        x += (f ? number : -number) * (color ? -number : number);
        y += (u ? number : -number);
        if (touching(x,y) && (board[x][y] == null))
        {
            newBoard(test, x, y);
            return true;
        }
        System.out.println("Invalid, please pick the other direction.");
        return false;
    }

    public void newBoard(Pieces test, int x, int y)
    {
        board[test.getX()][test.getY()] = null;
        board[x][y] = test;
        test.setX(x);
        test.setY(y);
        if ((x == 0 && test.getColor()) ||
                (x == 7 && !test.getColor()))
        {
            //when moves is 7, it has to be king bc it can only move forward, max 7 times
            test.setIsKing(true);
        }
        System.out.println();
        printBoard();
    }
    public void openSpots(int test)
    {
        count = 0;
        Pieces change;
        int totalPieces = 12;
        for(int i = 0; i < totalPieces; i++)
        {
            change = current[test][i];
            if(change.isOnboard())
            {
                findJump(change, false);
                if (change.a())
                {
                    b[count] = change;
                    count++;
                }
            }
        }
    }
    public void findJump(Pieces test, boolean w)
    {
        Pieces jump;

        kValue = 0;
        test.setA(false);
        for (int r = 1; r >=-1; r-=2)
        {
            for (int c = 1; c >= -1; c -= 2)
            {
                jump = canJump(test,r,c);
                if (jump != null)
                    if (w) {
                        k[kValue++] = jump;
                    } else {
                        return;
                    }
            }
            if (!test.isKing()) break;
        }
    }

    public void pickMethod()
    {
        int x;
        Pieces a, c;
        a = selector(b, count);
        findJump(a, true);
        while(kValue > 0)
        {
            c = jumpcon(a);
            jump(a,c);
            findJump(a, true);
        }
    }
    public void jump(Pieces test, Pieces jumped)
    {
        int rrr = 2;

        int x = test.getX();
        int y = test.getY();

        int jumpx = jumped.getX();
        int jumpy = jumped.getY();

        int xx = x +(jumpx-x)*rrr;
        int yy = y +(jumpy-y)*rrr;
        board[jumpx][jumpy] = null;
        jumped.setOnboard(false);

        String jump = (test.getSymbol() + " can jump over " + jumped.getSymbol() + ". Press enter to execute.");

        System.out.println(jump);
        uggs.nextLine();
        newBoard(test,xx,yy);
    }

    public Pieces jumpcon(Pieces test)
    {
        Pieces jumped;
        if (kValue > 1) { // can be jumped
            String cut = ("Select the cutting piece from " + test.getSymbol());
            System.out.println(cut);
            jumped = selector(k,kValue);
        } else {
            jumped = k[0];
        }
        return jumped;
    }

    public Pieces selector(Pieces [] test, int number)
    {
        int x;
        if (number == 1) // possible options for jumping
        {
            return test[0];
        }
        int p = 1;
        while (true) {
            System.out.println("Pick the option from 1-" + number);
            for (int i = 0; i < number; i++) {
                System.out.println(i + p + " " + test[i].getSymbol());
            }
            x = numberTest()-p;
            if (x < 0 || x >= number)
            {
                System.out.println("Invalid, please pick a number that is shown");
                continue;
            }
            return test[x];
        }
    }

    public int numberTest() throws InputMismatchException
    {
        while (true)
        {
            try
            {
                int x = uggs.nextInt();
                uggs.nextLine();
                return x;
            } catch (InputMismatchException e)
            {
                uggs.nextLine();
                System.out.println("Invalid, please pick a different number");
            }
        }
    }


}