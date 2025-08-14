package br.com.alx.hangman;

import br.com.alx.hangman.exception.GameIsFinishedException;
import br.com.alx.hangman.exception.LetterAlreadyInputException;
import br.com.alx.hangman.model.HangmanChar;
import br.com.alx.hangman.model.HangmanGame;

import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String... args) {
        var characters = Stream.of(args)
                .filter(a -> !a.isEmpty())
                .map(a -> a.toLowerCase().charAt(0))
                .map(HangmanChar::new)
                .toList();
        System.out.println(characters);
        var hangmanGame = new HangmanGame(characters);
        System.out.println("Bem vindo ao jogo da forca!, tente advinhar a palavra, boa sorte!.");
        System.out.println(hangmanGame);
;
        var option = -1;
        while (true) {
            System.out.println("Selecione uma das opções:");
            System.out.println("1 - Digitar uma letra");
            System.out.println("2 - Verificar o status do jogo");
            System.out.println("0 - Sair do jogo");

            option = scanner.nextInt();
            switch (option) {
                case 1 -> inputCharacter(hangmanGame);
                case 2 -> showGameStatus(hangmanGame);
                case 0 -> {
                    System.out.println("Saindo do jogo, até mais!");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    private static void showGameStatus(HangmanGame hangmanGame) {
        System.out.println(hangmanGame.getHangmanGameStatus());
        System.out.println(hangmanGame);
    }

    private static void inputCharacter(HangmanGame hangmanGame) {
        System.out.println("Digite uma letra:");
        var character = scanner.next().toLowerCase().charAt(0);
        try {
            hangmanGame.inputCharacter(character);
        }
        catch (LetterAlreadyInputException ex){
            System.out.println(ex.getMessage());
            System.out.println(hangmanGame);
        }
        catch (GameIsFinishedException ex){
            System.out.println(ex.getMessage());
            System.exit(0);
        }
        System.out.println(hangmanGame);
    }
}
