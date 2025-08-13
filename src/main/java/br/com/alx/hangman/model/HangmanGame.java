package br.com.alx.hangman.model;

import java.util.List;

import static br.com.alx.hangman.model.HangmanGameStatus.PENDING;

public class HangmanGame {

    private final static int HANGMAN_INITIAL_LINE_LENGTH = 9;
    private final static int HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR = 10;

    private final int linesize;
    private final List<HangmanChar> characters;

    private String hangman;
    private HangmanGameStatus hangmanGameStatus;

    public HangmanGame(final List<HangmanChar> characters) {

        var whiteSpaces = "".repeat(characters.size());
        var characterSpace = "_".repeat(characters.size());
        this.linesize = HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR + whiteSpaces.length() + characterSpace.length();
        this.hangmanGameStatus = PENDING;
        buildHangmanDesign(whiteSpaces, characterSpace);
        this.characters = setCharacterSpacesPositionInGame(characters, whiteSpaces.length());
    }

    @Override
    public String toString() {
        return hangman;
    }

    private List<HangmanChar> setCharacterSpacesPositionInGame(final List<HangmanChar> characters, final int whiteSpacesAmount) {

        final var LINE_LETTER = 6;

        for (int i = 0; i < characters.size(); i++) {
            characters.get(i).setPosition(this.linesize * LINE_LETTER + HANGMAN_INITIAL_LINE_LENGTH);
        }

        return characters;
    }



    private void buildHangmanDesign(final String whitespaces, final String characterSpaces){
        hangman = "  ----- " + whitespaces + System.lineSeparator() +
                  "  |   | " + whitespaces + System.lineSeparator() +
                  "  |   | " + whitespaces + System.lineSeparator() +
                  "  |     " + whitespaces + System.lineSeparator() +
                  "  |     " + whitespaces + System.lineSeparator() +
                  "  |     " + whitespaces + System.lineSeparator() +
                  "  |     " + whitespaces + System.lineSeparator() +
                  "=========" + characterSpaces + System.lineSeparator();
    }
}
