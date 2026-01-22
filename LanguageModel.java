import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    HashMap<String, List> CharDataMap;
    int windowLength;
    private Random randomGenerator;

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<>();
    }

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<>();
    }

    public void train(String fileName) {
        In in = new In(fileName);
        String window = "";

        for (int i = 0; i < windowLength; i++) {
            window += in.readChar();
        }

        while (!in.isEmpty()) {
            char c = in.readChar();
            List probs = CharDataMap.get(window);

            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }

            probs.update(c);
            window = window.substring(1) + c;
        }

        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    public void calculateProbabilities(List probs) {
        int total = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            total += probs.get(i).count;
        }

        double cumulative = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / total;
            cumulative += cd.p;
            cd.cp = cumulative;
        }
    }

    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); i++) {
            if (r < probs.get(i).cp) {
                return probs.get(i).chr;
            }
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) return initialText;

        String text = initialText;

        while (text.length() < textLength) {
            String window = text.substring(text.length() - windowLength);
            List probs = CharDataMap.get(window);
            if (probs == null) break;
            text += getRandomChar(probs);
        }
        return text;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : CharDataMap.keySet()) {
            sb.append(key).append(" : ").append(CharDataMap.get(key)).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int textLength = Integer.parseInt(args[2]);
        String fileName = args[4];

        LanguageModel lm = new LanguageModel(windowLength);
        lm.train(fileName);
        System.out.println(lm.generate(initialText, textLength));
    }
}
