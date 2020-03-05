package vista;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to ask input to user.
 *
 * @author mfontana
 */
public class InputAsker {

    /**
     *
     * @param message
     * @param max
     * @return
     * @throws ExceptionStucomZen
     */
    public static String askString(String message, int max) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        do {
            try {
                System.out.println(message);
                answer = br.readLine();
                if (answer.length() > max) {
                    //throw new ExceptionStucomZen(ExceptionStucomZen.maxStringLength);
                }
                if (answer.equals("")) {
                    System.out.println("You must write something.");
                }
            } catch (IOException ex) {
                System.out.println("Error input / output.");
            }
        } while (answer.equals(""));
        return answer;
    }

    /**
     *
     * @param message
     * @return
     */
    public static String askDNI(String message) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Pattern pat = Pattern.compile("[0-9]{7,8}[A-Za-z]");
        String answer = "";
        boolean error = true;
        do {
            try {
                System.out.println(message);
                answer = br.readLine();
                if (answer.length() != 9) {
                    System.out.println("DNI must be 9 digits (8 numbers and 1 character).");
                } else {
                    if (pat.matcher(answer).matches()) {
                        error = false;
                    } else {
                        System.out.println("Incorrect DNI pattern.");
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error input / output.");
            }
        } while (error);
        return answer;
    }
    
    /**
     *
     * @param message
     * @return
     */
    public static Boolean checkDNINumber(String dni) {
        Pattern pat = Pattern.compile("[0-9]{8}");
        if (pat.matcher(dni).matches()) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param message
     * @return
     */
    public static String askPostalCode(String message) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Pattern pat = Pattern.compile("(\\d{5})");
        String answer = "";
        boolean error = true;
        do {
            try {
                System.out.println(message);
                answer = br.readLine();
                if (pat.matcher(answer).matches()) {
                    error = false;
                } else {
                    System.out.println("Incorrect postal code pattern.");
                }
            } catch (IOException ex) {
                System.out.println("Error input / output.");
            }
        } while (error);
        return answer;
    }
    
    /**
     * 
     * @param message
     * @param max
     * @return 
     */
    public static int askInt(String message, int max) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int num = 0;
        boolean error = false;
        do {
            try {
                System.out.println(message);
                num = Integer.parseInt(br.readLine());
                if (String.valueOf(num).length() <= 11) {
                    error = false;
                } else {
                    //throw new ExceptionStucomZen(ExceptionStucomZen.maxIntLength);
                }
            } catch (IOException ex) {
                System.out.println("Error input / output.");
                error = true;
            } catch (NumberFormatException ex) {
                System.out.println("Please, write integer number.");
                error = true;
            }
        } while (error);
        return num;
    }

    /**
     *
     * @param message
     * @param max
     * @return
     */
    public static double askDouble(String message, int max) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        double num = 0;
        boolean error = true;
        do {
            try {
                System.out.println(message);
                num = Double.parseDouble(br.readLine());
                if (String.valueOf(num).length() <= 4) {
                    error = false;
                } else {
                    //throw new ExceptionStucomZen(ExceptionStucomZen.maxIntLength);
                }
            } catch (IOException ex) {
                System.out.println("Error input / output.");
                error = true;
            } catch (NumberFormatException ex) {
                System.out.println("Please, write integer number.");
                error = true;
            }
        } while (error);
        return num;
    }

    /**
     * Request String
     *
     * @param message
     * @return String
     */
    public static String askString(String message) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        do {
            try {
                System.out.println(message);
                answer = br.readLine();
                if (answer.equals("")) {
                    System.out.println("You must write something.");
                }
            } catch (IOException ex) {
                System.out.println("Error input / output.");
            }
        } while (answer.equals(""));
        return answer;
    }

    /**
     * Request String, it can only be optionA or optionB
     *
     * @param message String
     * @param optionA String
     * @param optionB String
     * @return String (optionA or optionB)
     */
    public static String askString(String message, String optionA, String optionB) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        do {
            answer = askString(message);
            if (!answer.equalsIgnoreCase(optionA) && !answer.equalsIgnoreCase(optionB)) {
                System.out.println("Wrong answer! Write " + optionA + " or " + optionB + " please");
            }
        } while (!answer.equalsIgnoreCase(optionA) && !answer.equalsIgnoreCase(optionB));
        return answer;
    }

    /**
     * Request String from a ArrayList of String accepteds
     *
     * @param message String
     * @param wordsAccepted ArrayList of String
     * @return String
     */
    public static String askString(String message, ArrayList<String> wordsAccepted) {
        String answer;
        boolean wordOk;
        do {
            for (String word : wordsAccepted) {
                System.out.println(word);
            }
            answer = InputAsker.askString(message);
            wordOk = wordIsOk(answer, wordsAccepted);
            if (!wordOk) {
                System.out.println("Wrong answer!");
            }
        } while (!wordOk);
        return answer;
    }

    /**
     * Returns true if the word exists in wordsAccepted.
     *
     * @param word
     * @param wordsAccepted
     * @return boolean
     */
    private static boolean wordIsOk(String word, ArrayList<String> wordsAccepted) {
        for (String w : wordsAccepted) {
            if (w.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Request int
     *
     * @param message
     * @return int
     */
    public static int askInt(String message) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int num = 0;
        boolean error;
        do {
            try {
                System.out.println(message);
                num = Integer.parseInt(br.readLine());
                error = false;
            } catch (IOException ex) {
                System.out.println("Error input / output.");
                error = true;
            } catch (NumberFormatException ex) {
                System.out.println("Please, write integer number.");
                error = true;
            }
        } while (error);
        return num;
    }

    /**
     *
     * @param message
     * @return
     */
    public static double askDouble(String message) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        double num = 0;
        boolean error;
        do {
            try {
                System.out.println(message);
                num = Double.parseDouble(br.readLine());
                error = false;
            } catch (IOException ex) {
                System.out.println("Error input / output.");
                error = true;
            } catch (NumberFormatException ex) {
                System.out.println("Please, write integer number.");
                error = true;
            }
        } while (error);
        return num;
    }

    /**
     * Request int in the interval between min and max (inclusives)
     *
     * @param message
     * @param min
     * @param max
     * @return int
     */
    public static int askInt(String message, int min, int max) {
        int num;
        do {
            num = askInt(message);
            if (num < min || num > max) {
                System.out.println("Error, the number must be between " + min + " and " + max);
            }
        } while (num < min || num > max);
        return num;
    }
    /**
     * 
     * @param message
     * @param equal
     * @return 
     */
    public static int askIntEqual(String message, int equal) {
        int num;
        do {
            num = askInt(message);
            
            if (String.valueOf(num).length()!= equal) {
                System.out.println("Error, the number must be between " + equal);
            }
        } while (String.valueOf(num).length()!= equal);
        return num;
    }

}
