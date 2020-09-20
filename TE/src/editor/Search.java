package editor;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Search extends Thread {

    String text;
    String search;
    boolean isRegex;
    ArrayList<Integer> index = new ArrayList<>();
    ArrayList<Integer> length = new ArrayList<>();
    int[][] indexAndLength;


    public Search(String textToSearch, String searchForThisText, boolean useRegEx) {
        this.text = textToSearch;
        this.search = searchForThisText;
        this.isRegex = useRegEx;
    }

    private void searchRegEX () {
        Pattern pattern = Pattern.compile(search);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            index.add(result.start());
            length.add((result.end() - result.start()));
        }
    }

    private void searchPlain () {
        while (text.indexOf(search) > 0) {
            index.add(text.indexOf(search));
            length.add(search.length());
            text = text.substring(text.indexOf(search) + search.length());
        }
    }

    @Override
    public void run() {
        searchRegEX();
        indexAndLength = new int[index.size()][2];
        for (int ii = 0; ii < index.size(); ii++) {
            indexAndLength[ii][0] = index.get(ii);
            indexAndLength[ii][1] = length.get(ii);
        }
    }
}
