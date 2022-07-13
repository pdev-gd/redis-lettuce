import java.util.ArrayList;

public class Util {

    public Util () {

    }

    public int checkInt(String val) {
        try {
            int i = Integer.parseInt(val);
            return i;
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    public long checkLong(String val) {
        try {
            long l = Long.parseLong(val);
            return l;
        } catch (NumberFormatException e) {
            return Long.MIN_VALUE;
        }
    }

    public double checkDouble(String val) {
        try {
            double d = Double.parseDouble(val);
            return d;
        } catch (NumberFormatException e) {
            return Double.MIN_VALUE;
        }
    }

    // space(°ø¶õ)ÀÌ ¿©·¯°³ ÀÖ¾îµµ Ã³¸®ÇÏ°í "str1 str2"µµ ÇÑ ´Ü¾î·Î Ã³¸®ÇÑ´Ù.
    // https://code-examples.net/ko/q/335d89
    public String[] split(String str) {
        str += " "; // To detect last token when not quoted...
        ArrayList<String> strings = new ArrayList<String>();
        boolean inQuote = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '"' || c == ' ' && !inQuote) {
                if (c == '"')
                    inQuote = !inQuote;
                if (!inQuote && sb.length() > 0) {
                    strings.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else
                sb.append(c);
        }
        return strings.toArray(new String[strings.size()]);
    }

}