package burp;

import java.util.Random;

public class CarPlateGenerator implements IIntruderPayloadGenerator, IIntruderPayloadGeneratorFactory
{
    private boolean mercosul;
    private int payloadIndex;

    public CarPlateGenerator(boolean mercosul)
    {
        this.mercosul = mercosul;
    }

    @Override
    public String getGeneratorName()
    {
        if (mercosul)
        {
            return "[BRBurp] Car Plate - Mercosul";
        }
        else
        {
            return "[BRBurp] Car Plate - Old";
        }
    }

    @Override
    public IIntruderPayloadGenerator createNewInstance(IIntruderAttack attack)
    {
        return new CarPlateGenerator(mercosul);
    }

    @Override
    public boolean hasMorePayloads()
    {
        return true;
    }

    @Override
    public byte[] getNextPayload(byte[] baseValue)
    {
        if (mercosul) {
            byte[] payload = generateMercosulCarPlate().getBytes();
            payloadIndex++;
            return payload;
        } else {
            byte[] payload = generateOldCarPlate().getBytes();
            payloadIndex++;
            return payload;
        }
    }

    @Override
    public void reset()
    {
        payloadIndex = 0;
    }

    // ==============================================
    // Car Plate Stuff
    // ==============================================
    private String generateOldCarPlate() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        // (A-Z)
        for (int i = 0; i < 3; i++) {
            char randomChar = (char) ('A' + rand.nextInt(26));
            sb.append(randomChar);
        }

        // (0-9)
        for (int i = 0; i < 4; i++) {
            int randomDigit = rand.nextInt(10);
            sb.append(randomDigit);
        }

        return sb.toString();
    }

    public static String generateMercosulCarPlate() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        // (A-Z)
        for (int i = 0; i < 3; i++) {
            char randomChar = (char) ('A' + rand.nextInt(26));
            sb.append(randomChar);
        }

        // (0-9)
        int randomDigit = rand.nextInt(10);
        sb.append(randomDigit);

        // (A-Z)
        char randomChar = (char) ('A' + rand.nextInt(26));
        sb.append(randomChar);

        // (0-9)
        randomDigit = rand.nextInt(10);
        sb.append(randomDigit);

        // (0-9)
        randomDigit = rand.nextInt(10);
        sb.append(randomDigit);

        return sb.toString();
    }
}
