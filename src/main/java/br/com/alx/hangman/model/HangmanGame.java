package br.com.alx.hangman.model;

import br.com.alx.hangman.exception.GameIsFinishedException;
import br.com.alx.hangman.exception.LetterAlreadyInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static br.com.alx.hangman.model.HangmanGameStatus.*;

public class HangmanGame {

    private final static int HANGMAN_INITIAL_LINE_LENGTH = 9;
    private final static int HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR = 10;

    private final int linesize;
    private final int hangmanSize;
    private final List<HangmanChar> hangmanPaths;
    private final List<HangmanChar> characters;
    private final List<Character> failAttempts = new ArrayList<>();

    private String hangman;
    private HangmanGameStatus hangmanGameStatus;

    public HangmanGame(final List<HangmanChar> characters) {

        var whiteSpaces = "".repeat(characters.size());
        var characterSpace = "_".repeat(characters.size());
        this.linesize = HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR + whiteSpaces.length() + characterSpace.length();
        this.hangmanGameStatus = PENDING;
        this.hangmanPaths = buildHangmanPathsPosition();
        buildHangmanDesign(whiteSpaces, characterSpace);
        this.characters = setCharacterSpacesPositionInGame(characters, whiteSpaces.length());
        this.hangmanSize = hangman.length();
    }

    public HangmanGameStatus getHangmanGameStatus() {
        return hangmanGameStatus;
    }

    public void  inputCharacter(final char character){
        if(this.hangmanGameStatus != PENDING){
            var message = this.hangmanGameStatus == WIN ?
                    "Parabens você ganhou!" :
                    "Você perdeu, tente novamente!";
            throw new GameIsFinishedException(message);
        }

        var found = this.characters.stream()
                .filter( c -> c.getCharacter() == character)
                .toList();

        if( this.failAttempts.contains(character) ){
            throw new LetterAlreadyInputException("A letra '" + character + "' já foi informada.");
        }
        if(found.isEmpty()){
            failAttempts.add(character);
            if (failAttempts.size() >= 6){
                this.hangmanGameStatus = LOSE;
            }
            rebuildHangman(this.hangmanPaths.remove(0));
            return;
        }
        if(found.get(0).isVisible()){
            throw new LetterAlreadyInputException("A letra '" + character + "' já foi informada.");
        }

        this.characters.forEach( c -> {
            if (c.getCharacter() == found.get(0).getCharacter()) {
                c.enableVisibility();
            }
        });

        if(this.characters.stream().noneMatch(HangmanChar::isInVisible)){
            this.hangmanGameStatus = WIN;
        }
        rebuildHangman(found.toArray(HangmanChar[]::new));
    }

    @Override
    public String toString() {
        return hangman;
    }

    private List<HangmanChar> buildHangmanPathsPosition(){
        final var HEAD_LINE = 3;
        final var BODY_LINE = 4;
        final var lEGS_LINE = 5;

        return  new ArrayList<>(
                List.of(
                        new HangmanChar('0', this.linesize * HEAD_LINE + 6),
                        new HangmanChar('|', this.linesize * BODY_LINE + 6),
                        new HangmanChar('/', this.linesize * BODY_LINE + 5),
                        new HangmanChar('\\', this.linesize * BODY_LINE + 7),
                        new HangmanChar('/', this.linesize * lEGS_LINE + 5),
                        new HangmanChar('\\', this.linesize * lEGS_LINE + 7)
                )
        );
    }

    private void rebuildHangman(final HangmanChar... hangmanChars){
        var hangmanBuilder = new StringBuilder(this.hangman);
        Stream.of(hangmanChars).forEach(
                h -> hangmanBuilder.setCharAt(h.getPosition(), h.getCharacter()
                ));
        var failMessage = this.failAttempts.isEmpty() ? "" : "Tentativas" + this.failAttempts;
        this.hangman = hangmanBuilder.substring(0, hangmanSize) + failMessage;
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
