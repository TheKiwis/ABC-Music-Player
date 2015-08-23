package player;

import org.junit.Test;
import player.ast.*;

import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testBasenote() throws Exception
    {
        Parser pars = getParser("A");
        assertEquals(new Basenote('A'), pars.expectBasenote());
    }


    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testBaseNoteFail() throws Exception
    {
        Parser pars = getParser("~");
        pars.expectBasenote();
    }

    @Test(expected = Lexer.RunOutOfTokenException.class)
    public void testBaseNoteEmptyLexer() throws Exception
    {
        Parser pars = getParser("");

        pars.expectBasenote();
    }


    @Test
    public void testAccidental() throws Exception
    {
        Parser pars = getParser("^^");
        assertEquals(Accidental.getInstance(Accidental.Type.DOUBLE_SHARP), pars.expectAccidental());

        pars = getParser("__");
        assertEquals(Accidental.getInstance(Accidental.Type.DOUBLE_FLAT), pars.expectAccidental());

        pars = getParser("=");
        assertEquals(Accidental.getInstance(Accidental.Type.NEUTRAL), pars.expectAccidental());
    }


    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testAccidentalFail() throws Exception
    {
        Parser pars = getParser("!");

        pars.expectAccidental();
    }

    @Test
    public void testOctave() throws Exception
    {
        Parser parser  = getParser("'''");
        assertEquals(Octave.getUp(3), parser.expectOctave());

        parser  = getParser(",,");
        assertEquals(Octave.getDown(2), parser.expectOctave());
    }


    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testOctaveFail() throws Exception
    {
        Parser pars  = getParser("xx");
        pars.expectOctave();
    }

    @Test
    public void testPitch() throws Exception
    {
        Parser parser = getParser("B");
        assertEquals(new Pitch(new Basenote('B'), Accidental.getEmptyObj(), Octave.getEmpty()), parser.expectPitch());

        parser = getParser("__c");
        assertEquals(new Pitch(new Basenote('c'), Accidental.getInstance(Accidental.Type.DOUBLE_FLAT), Octave.getEmpty()), parser.expectPitch());

        parser = getParser("^A,");
        assertEquals(new Pitch(new Basenote('A'), Accidental.getInstance(Accidental.Type.SHARP), Octave.getDown(1)), parser.expectPitch());

        parser = getParser("B'");
        assertEquals(new Pitch(new Basenote('B'), Accidental.getEmptyObj(), Octave.getUp(1)), parser.expectPitch());
    }

    @Test
    public void testExpectRest() throws Exception
    {
        Parser parser = getParser("z");
        assertEquals(Rest.getInstance(), parser.expectRest());
    }

    @Test
    public void testExpectNoteLength() throws Exception
    {
        Parser parser = getParser("4");
        assertEquals(new NoteLength(4, 1), parser.expectNoteLength());

        parser = getParser("4/4");
        assertEquals(new NoteLength(4, 4), parser.expectNoteLength());

        parser = getParser("/3");
        assertEquals(new NoteLength(1, 3), parser.expectNoteLength());

        parser = getParser("/");
        assertEquals(new NoteLength(1, 2), parser.expectNoteLength());

        parser = getParser("4/");
        assertEquals(new NoteLength(4, 2), parser.expectNoteLength());
    }

    @Test
    public void testNoteOrRest() throws Exception
    {

        Parser parser = getParser("z");

        assertEquals(Rest.getInstance(), parser.expectNoteOrRest());

        parser = getParser("B");

        assertEquals(new Pitch(new Basenote('B'), Accidental.getEmptyObj(), Octave.getEmpty()), parser.expectNoteOrRest());

        parser = getParser("__c");
        assertEquals(new Pitch(new Basenote('c'), Accidental.getInstance(Accidental.Type.DOUBLE_FLAT), Octave.getEmpty()), parser.expectNoteOrRest());


        parser = getParser("^D,,");
        assertEquals(new Pitch(new Basenote('D'), Accidental.getInstance(Accidental.Type.SHARP), Octave.getDown(2)), parser.expectNoteOrRest());
    }

    public Parser getParser(String str)
    {
        return new Parser(new Lexer(str));
    }
}
