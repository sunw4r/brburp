package burp;

public class BurpExtender implements IBurpExtender
{
    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks)
    {
        callbacks.setExtensionName("BRBurp");
        //Payloads
        callbacks.registerIntruderPayloadGeneratorFactory(new CPFGenerator(false));
        callbacks.registerIntruderPayloadGeneratorFactory(new CPFGenerator(true));
        callbacks.registerIntruderPayloadGeneratorFactory(new CNPJGenerator(false));
        callbacks.registerIntruderPayloadGeneratorFactory(new CNPJGenerator(true));
        callbacks.registerIntruderPayloadGeneratorFactory(new CarPlateGenerator(false));
        callbacks.registerIntruderPayloadGeneratorFactory(new CarPlateGenerator(true));
        callbacks.registerIntruderPayloadGeneratorFactory(new BrNameGenerator(false, true));
        callbacks.registerIntruderPayloadGeneratorFactory(new BrNameGenerator(false, false));
        callbacks.registerIntruderPayloadGeneratorFactory(new BrNameGenerator(true, true));
        callbacks.registerIntruderPayloadGeneratorFactory(new BrNameGenerator(true, false));
        //Passive Scan
        callbacks.registerScannerCheck(new PassiveScanCPF(callbacks));
    }
}