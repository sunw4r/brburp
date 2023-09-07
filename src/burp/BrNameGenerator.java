package burp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BrNameGenerator implements IIntruderPayloadGenerator, IIntruderPayloadGeneratorFactory {

    private List<String> payloads;
    private boolean top100;
    private boolean lastname;
    private int payloadIndex;

    private static final String FILE_FIRSTNAME          = "firstname_ptbr.txt";
    private static final String FILE_FIRSTNAME_TOP100   = "firstname_ptbr_100.txt";
    private static final String FILE_LASTNAME           = "surname_ptbr.txt";
    private static final String FILE_LASTNAME_TOP100    = "surname_ptbr_100.txt";

    public BrNameGenerator(boolean lastname, boolean top100)
    {
        this.top100 = top100;
        this.lastname = lastname;

        String filetarget = FILE_FIRSTNAME;
        if (top100) {
            if (lastname) {
                filetarget = FILE_LASTNAME_TOP100;
            }
            filetarget = FILE_FIRSTNAME_TOP100;
        }
        if (lastname) {
            filetarget = FILE_LASTNAME;
        }

        payloads = new ArrayList<>();

        InputStream in = getClass().getResourceAsStream("/"+filetarget);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = br.readLine()) != null) {
                payloads.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }

    @Override
    public String getGeneratorName() {
        if (top100) {
            if (lastname) {
                return "[BRBurp] Last Name - Top 100";
            }
            return "[BRBurp] First Name - Top 100";
        }
        if (lastname) {
            return "[BRBurp] Last Name - Common ("+payloads.size()+")";
        }
        return "[BRBurp] First Name - Common ("+payloads.size()+")";
    }

    @Override
    public IIntruderPayloadGenerator createNewInstance(IIntruderAttack attack) {
        return new BrNameGenerator(lastname, top100);
    }

    @Override
    public boolean hasMorePayloads() {
        return payloadIndex < payloads.size();
    }

    @Override
    public byte[] getNextPayload(byte[] baseValue) {
        String payload = payloads.get(payloadIndex);
        payloadIndex++;
        return payload.getBytes();
    }

    @Override
    public void reset() {
        this.payloadIndex = 0;
    }


}
