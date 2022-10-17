public class Pieces
{
    private int x;
    private int y;
    private boolean a;
    private String symbol;
    private boolean onboard;
    private boolean color;
    private boolean isKing;

    public Pieces(int r, int c, boolean color, int pieceNumber)
    {
        this.x = r;
        this.y = c;
        String s = (color ? "B" : "W") + (pieceNumber < 10?"0" + pieceNumber:pieceNumber);
        this.symbol = s;
        this.isKing = false;
        this.onboard = true;
        this.color = color;
    }

    //getters

    public String getSymbol()
    {
        return symbol;
    }

    public boolean isKing()
    {
        return isKing;
    }

    public boolean isOnboard()
    {
        return onboard;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public boolean getColor()
    {
        return color;
    }
    public boolean a()
    {
        return a;
    }

    //setters

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setOnboard(boolean onboard)
    {
        this.onboard = onboard;
    }

    public void setIsKing(boolean isKing)
    {
        this.isKing = isKing;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public void setA(boolean a)
    {
        this.a = a;
    }

}