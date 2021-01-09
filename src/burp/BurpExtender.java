package burp;

public class BurpExtender implements IBurpExtender
{
    private IExtensionHelpers helpers;

    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks)
    {
        helpers = callbacks.getHelpers();

        callbacks.setExtensionName("BRBurp Payloads");
        callbacks.registerIntruderPayloadGeneratorFactory(new CPFGenerator(false));
        callbacks.registerIntruderPayloadGeneratorFactory(new CPFGenerator(true));
        callbacks.registerIntruderPayloadGeneratorFactory(new CNPJGenerator(false));
        callbacks.registerIntruderPayloadGeneratorFactory(new CNPJGenerator(true));
    }

}