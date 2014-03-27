/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.xxyy.common.tests;

/**
 *
 * @author xxyy98 (http://xxyy.github.io/)
 */
public class PlayerWrapperTest extends XyCommonTest {
/*
    private PlayerWrapper wrp;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        wrp = PlayerWrapperFactory.getGenericFactory().getWrapper("xxyy98"); //Hope
        Assert.assertNotNull("Wrapper is null!", wrp);
    }

    @Test
    public void testForceFullFlush() {
        System.out.println("Now testing flushing and fetching...");
        wrp.forceFullFetch();
        wrp.forceFullFlush();
    }

    @Test
    public void testGetPassesUsed() {
        System.out.println("Now testing passes...");
        printPasses();
        int currentPasses = wrp.getPassesAmount();
        int currentPassesUsed = wrp.getPassesUsed();
        wrp.addPassUse();
        printPasses();
        Assert.assertTrue("Incorrect pass amount!", (currentPasses - 1) == wrp.getPassesAmount());
        Assert.assertTrue("Incorrect pass amount!", (currentPassesUsed + 1) == wrp.getPassesUsed());
        wrp.summonPasses(-1);
        printPasses();
    }

    private void printPasses() {
        System.out.println("passes:" + wrp.getPassesAmount() + ";used:" + wrp.getPassesUsed());
    }

    @Test
    public void testPermissions() {
        System.out.println("Now testing getGroup()...");
        System.out.println("Does wrp's group have permission 'stuff'? " + wrp.getGroup().hasPermission("stuff"));
    }

    @Test
    public void testNick() {
        System.out.println("Now testing setNick(String) and getNick()...");
        String currentNick = printCurrentNick();
        wrp.setNick("Chuck");
        if (!"Chuck".equals(currentNick)) {
            Assert.assertFalse("Nick remained unchanged.", currentNick.equals(printCurrentNick()));
        }
        wrp.setNick(currentNick);
        printCurrentNick();
    }

    private String printCurrentNick() {
        String currentNick = wrp.getNick();
        System.out.println("Current nick: " + currentNick);
        return currentNick;
    }

    @Test
    public void testGetAnyName() {
        System.out.println("Now testing getAnyName(String)...");
        System.out.println("'xxyy98'->" + PlayerWrapper.getAnyName("xxyy98"));
    }

    @Test
    public void testGetNameByNick() {
        System.out.println("Now testing getNameByNick(String)...");
        System.out.println("'xxyy'->" + PlayerWrapper.getNameByNick("xxyy"));
    }*/
}
