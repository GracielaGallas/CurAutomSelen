package tests;


import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import suporte.Generator;
import suporte.Screenshot;
import suporte.Web;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


@RunWith(DataDrivenTestRunner.class)
@DataLoader(filePaths = "InformacoesUsuarioTest.csv")
public class InformacoesUsuarioTest {

    private WebDriver navegador;

    @Rule
    public TestName test = new TestName();

    @Before
    public void setUp(){
        navegador = Web.createChrome();

        //clicar no link q possui o texto "Sign in"
        navegador.findElement(By.linkText("Sign in")).click();

        //identificando o formulário de login
        WebElement formularioSignInBox = navegador.findElement(By.id("signinbox"));

        //digitar no campo name "login" que está dentro do formulário de id = "signinbox" o texto "julio0001"
        formularioSignInBox.findElement(By.name("login")).sendKeys("julio0001");

        //digitar no campo name "password" que está dentro do formulário de id = "signinbox" o texto "123456"
        formularioSignInBox.findElement(By.name("password")).sendKeys("123456");

        //clicar no link com o texto "SIGN IN"
        navegador.findElement(By.linkText("SIGN IN")).click();

        // validar que dentro do elemento com class "me" está o texto "Hi, Julio"
        WebElement me = navegador.findElement(By.className("me"));
        String textoNoElementoMe = me.getText();
        assertEquals("Hi, Julio", textoNoElementoMe);

        //clicar em um link que possui a class "me"
        navegador.findElement(By.className("me")).click();

        //clicar em um link que tem o texto "MORE DATA ABOUT YOU"
        navegador.findElement(By.linkText("MORE DATA ABOUT YOU")).click();
    }

    @Test
    public void testAdicionarUmaInformacaoAdicionalDoUsuario(@Param(name="tipo")String tipo, @Param(name="contato")String contato,@Param(name="mensagem")String mensagemEsperada)
            throws InterruptedException {
        //clicar no botão através do seu xpath (//div[@id="moredata"]//button[@data-target="addmoredata"])
        navegador.findElement(By.xpath("//div[@id='moredata']//button[@data-target='addmoredata']")).click();

        //identificar a popup onde está o formulário de ID "addmoredata"
        WebElement popupAddMoreData = navegador.findElement(By.id("addmoredata"));

        //na combo de name "type"escolher a opção "Phone"
        WebElement campoType = popupAddMoreData.findElement(By.name("type"));
        new Select(campoType).selectByVisibleText(tipo);

        // no campo de name "contact" digitar "5551123456789"
        popupAddMoreData.findElement(By.name("contact")).sendKeys(contato);

        //clicar no link de text save que está na popup
        popupAddMoreData.findElement(By.linkText("SAVE")).click();

        // Na mensagem de ID "toast-container" validar que o texto é "Your contact has been added!"
        WebElement mensagemPop = navegador.findElement(By.id("toast-container"));
        String mensagem = mensagemPop.getText();
        assertEquals(mensagemEsperada, mensagem);


    }

    @Test
    public void removerUmContatoDeUmUsuario(){
        //clicar no elemento pelo seu xpath: span[text()='11111111111111111111111111111111']/following-sibling::a
        navegador.findElement(By.xpath("//span[text()='11111111111111111111111111111111']/following-sibling::a")).click();

        //confirmar a janela do Javascript
        navegador.switchTo().alert().accept();

        //validar que a mensagem apresentada foi : Rest inpeace, dear phone!
        WebElement mensagemPop = navegador.findElement(By.id("toast-container"));
        String mensagem = mensagemPop.getText();
        assertEquals("Rest in peace, dear phone!", mensagem);
        String screenshotArquivo = "C:\\Users\\carmen.graciela\\IdeaProjects\\CursoAutomacaoSWD\\results\\"
                + Generator.dataHoraParaArquivo()+ test.getMethodName() + ".png";
        Screenshot.tirar(navegador, screenshotArquivo);

        //aguardar até 10 segundos para que a janela desapareça
        WebDriverWait aguardar = new WebDriverWait(navegador, 10);
        aguardar.until(ExpectedConditions.stalenessOf(mensagemPop));

        //clicar no link com o texto  "Logout"
        navegador.findElement(By.linkText("Logout")).click();

    }





    @After
    public void tearDown(){
        //fechar o navegador
        navegador.close();
    }
}
