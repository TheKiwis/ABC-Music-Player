package player.ast;

public class Accidental implements AbstractSyntaxTree
{
    public enum Type
    {
        SHARP(1), DOUBLE_SHARP(2), FLAT(-1), DOUBLE_FLAT(-2), NEUTRAL(0), NONE(0);

        private final int amount;

        private Type(int amount)
        {
            this.amount = amount;
        }

        /**
         * @return amount of semitones this accidental gives to the note
         */
        public int getAmount()
        {
            return amount;
        }
    }

    /**
     * type of accidental
     */
    private final Type type;

    private static Accidental sharp;
    private static Accidental doubleSharp;
    private static Accidental flat;
    private static Accidental doubleFlat;
    private static Accidental neutral;
    private static Accidental none;

    public Accidental(Type type)
    {
        this.type = type;
    }

    public static Accidental getInstance(Type type)
    {
        switch (type) {
            case SHARP:
                return sharp = (sharp == null) ? new Accidental(Type.SHARP) : sharp;
            case DOUBLE_SHARP:
                return doubleSharp = (doubleSharp == null) ? new Accidental(Type.DOUBLE_SHARP) : doubleSharp;
            case FLAT:
                return flat = (flat == null) ? new Accidental(Type.FLAT) : flat;
            case DOUBLE_FLAT:
                return doubleFlat = (doubleFlat == null) ? new Accidental(Type.DOUBLE_FLAT) : doubleFlat;
            case NEUTRAL:
                return neutral = (neutral == null) ? new Accidental(Type.NEUTRAL) : neutral;
        }

        return none = (none == null) ? new Accidental(Type.NONE) : none;
    }

    /**
     * @return type of accidental
     */
    public Type getType()
    {
        return type;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Accidental that = (Accidental) o;

        return type == that.type;

    }

    /**
     * @return null object of Octave
     */
    public static Accidental getEmpty()
    {
        return Accidental.getInstance(Type.NONE);
    }


    @Override
    public int hashCode()
    {
        return type.hashCode();
    }

    @Override
    public String toString()
    {
        return "Accidental{" +
                "type=" + type +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
