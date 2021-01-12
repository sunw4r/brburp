package burp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PassiveScanCPF implements IScannerCheck
{
    IBurpExtenderCallbacks callbacks;

    public PassiveScanCPF(IBurpExtenderCallbacks callbacks)
    {
        this.callbacks = callbacks;
    }

    private List<int[]> getMatches(byte[] response)
    {
        Pattern CPFMinPunctPattern = Pattern.compile("(\\d{9}\\x2D\\d{2})");
        Pattern CPFPunctPattern = Pattern.compile("(\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2})");
        Pattern CPFPattern = Pattern.compile("(?<!\\d)(\\d{11})(?!\\d)");

        List<int[]> matches = new ArrayList<int[]>();

        String responseStr = callbacks.getHelpers().bytesToString(response);

        Matcher CPFPuncMatcher = CPFPunctPattern.matcher(responseStr);
        while (CPFPuncMatcher.find())
        {
            if (isCPF(CPFPuncMatcher.group()))
            {
                matches.add(new int[]{CPFPuncMatcher.start(), CPFPuncMatcher.end()});
            }
        }

        Matcher CPFMinPuctMatcher = CPFMinPunctPattern.matcher(responseStr);
        while (CPFMinPuctMatcher.find())
        {
            if (isCPF(CPFMinPuctMatcher.group()))
            {
                matches.add(new int[]{CPFMinPuctMatcher.start(), CPFMinPuctMatcher.end()});
            }
        }

        Matcher CPFmatcher = CPFPattern.matcher(responseStr);
        while (CPFmatcher.find())
        {
            if (isCPF(CPFmatcher.group()))
            {
                matches.add(new int[]{CPFmatcher.start(), CPFmatcher.end()});
            }
        }

        return matches;
    }

    @Override
    public List<IScanIssue> doPassiveScan(IHttpRequestResponse baseRequestResponse)
    {
        IExtensionHelpers helpers = callbacks.getHelpers();

        List<int[]> matches = getMatches(baseRequestResponse.getResponse());
        if (matches.size() > 0)
        {
            List<IScanIssue> issues = new ArrayList<>(1);
            issues.add(new CustomScanIssue(
                    baseRequestResponse.getHttpService(),
                    helpers.analyzeRequest(baseRequestResponse).getUrl(),
                    new IHttpRequestResponse[] { callbacks.applyMarkers(baseRequestResponse, null, matches) },
                    "CPF Found",
                    "The response contains CPF numbers.",
                    "Information",
                    "Certain"));
            return issues;
        }
        else return null;
    }

    @Override
    public List<IScanIssue> doActiveScan(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint)
    {
        //No active scan
        return null;
    }

    @Override
    public int consolidateDuplicateIssues(IScanIssue existingIssue, IScanIssue newIssue)
    {
        return 0;
    }

    //CPF Validação
    public static boolean isCPF(String CPF) {

        CPF = CPF.replace("-","").replace(".","");

        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }
}
