package player;

import player.ast.*;
import sound.SequencePlayer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class AbcPlayer implements AbcVisitor<Void>
{
    /**
     * SequencePlayer plays pitches and rest
     */
    private final SequencePlayer sequencePlayer;

    private static final RationalNumber CROCHET_NOTE_LENGTH = new RationalNumber(1, 4);

    private final RationalNumber minNoteLength;

    private RationalNumber defaultNoteLength;

    private int currentTick;

    public AbcPlayer(AbstractSyntaxTree ast, AbcInfoCollector abcInfoCollector) throws InvalidMidiDataException, MidiUnavailableException
    {
        int ticksPerQuarterNote = 1;

        currentTick = 0;

        defaultNoteLength = abcInfoCollector.getDefaultNoteLength();

        minNoteLength = abcInfoCollector.getMinNoteLength();

        if (CROCHET_NOTE_LENGTH.compareTo(minNoteLength) > 0) {
            // TODO: round it up!!!
            ticksPerQuarterNote = CROCHET_NOTE_LENGTH.divide(minNoteLength).getNumerator();
        }

        this.sequencePlayer = new SequencePlayer(abcInfoCollector.getBpm(), ticksPerQuarterNote);

        // start traverse the tree
        ast.accept(this);
    }

    @Override
    public Void on(AbcTune tune)
    {
        tune.getHeader().accept(this);
        tune.getBody().accept(this);

        return null;
    }

    @Override
    public Void on(AbcHeader header)
    {
        return null;
    }

    @Override
    public Void on(AbcMusic body)
    {
        body.getLines().stream().forEach(line -> line.accept(this));
        return null;
    }

    @Override
    public Void on(FieldNumber field)
    {
        return null;
    }

    @Override
    public Void on(FieldTitle field)
    {
        return null;
    }

    @Override
    public Void on(FieldKey field)
    {
        return null;
    }

    @Override
    public Void on(FieldVoice field)
    {
        return null;
    }

    @Override
    public Void on(FieldComposer field)
    {
        return null;
    }

    @Override
    public Void on(FieldDefaultLength field)
    {
        return null;
    }

    @Override
    public Void on(FieldMeter field)
    {
        return null;
    }

    @Override
    public Void on(FieldTempo field) { return null; }

    @Override
    public Void on(Comment c)
    {
        return null;
    }

    @Override
    public Void on(ElementLine line)
    {
        line.getElements().stream().forEach(element -> element.accept(this));
        return null;
    }

    @Override
    public Void on(Element element)
    {
        element.accept(this);
        return null;
    }

    @Override
    public Void on(NthRepeat repeat)
    {
        return null;
    }

    @Override
    public Void on(Barline bar)
    {
        return null;
    }

    @Override
    public Void on(TupletElement element)
    {
        return null;
    }

    @Override
    public Void on(MultiNote mnote)
    {
        mnote.getNotes().stream().forEach(note -> note.accept(this));
        return null;
    }

    @Override
    public Void on(NoteLength noteLength)
    {
        return null;
    }

    @Override
    public Void on(Rest rest)
    {
        return null;
    }

    @Override
    public Void on(Pitch pitch)
    {
        NoteLength nl = pitch.getNoteLength();

        RationalNumber rationalNoteLength = new RationalNumber(nl.getMultiplier(), nl.getDivider());

        RationalNumber realNoteLength = rationalNoteLength.multiply(defaultNoteLength);

        int ticks = realNoteLength.divide(minNoteLength).getNumerator();

        sound.Pitch soundPitch = new sound.Pitch(pitch.getBasenote().getSymbol());

        sequencePlayer.addNote(soundPitch.toMidiNote(), currentTick, ticks);

        currentTick += ticks;

        return null;
    }

    @Override
    public Void on(Basenote basenote)
    {
        return null;
    }

    @Override
    public Void on(Accidental acc)
    {
        return null;
    }

    @Override
    public Void on(Octave octave)
    {
        return null;
    }

    @Override
    public Void on(KeyAccidental keyAccidental)
    {
        return null;
    }

    @Override
    public Void on(Keynote keynote)
    {
        return null;
    }

    @Override
    public Void on(NoteLengthStrict noteLengthStrict)
    {
        return null;
    }

    @Override
    public Void on(ModeMinor modeMinor)
    {
        return null;
    }

    @Override
    public Void on(MeterFraction meterFraction)
    {
        return null;
    }

    @Override
    public Void on(MeterCPipe meterCPipe)
    {
        return null;
    }

    @Override
    public Void on(MeterC meterC)
    {
        return null;
    }

    @Override
    public Void on(Key key)
    {
        return null;
    }

    @Override
    public String toString()
    {
        return sequencePlayer.toString();
    }

    public void play() throws MidiUnavailableException
    {
        sequencePlayer.play();
    }
}
