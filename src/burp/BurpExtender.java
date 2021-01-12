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
        //Passive Scan
        callbacks.registerScannerCheck(new PassiveScanCPF(callbacks));
    }
}