// 1 - Pacote

// 2 - Bibliotecas

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class curso {
    // 3.1 - Atributos
    String url; // Endereço do SUT - Software Under Test - Software Sob Teste - Software Alvo
    WebDriver driver; // Objeto Selenium WebDriver
    static String caminhoPrint = new SimpleDateFormat("yyyy-MM-dd HH-mm").format(Calendar.getInstance().getTime());

    // 3.2 - Métodos e Funções

    /*
    TestNG oferece diversos momento de execução
    São chamadas de Anotações (Annotations) - precidas por @
    @BeforeSuite - Antes das Suites de Teste - Primeiro de Tudo
        @BeforeTest - Teste - testng.xml
            @BeforeClass - Classe
                @BeforeMethod - Método
                    @Test
                @AfterMethod
            @AfterClass
        @AfterTest
    @AfterSuite - Depois das Suites de Teste - Último de Todos

     */

    // Métodos e Funções de Apoio
    // Método de Print

    public void print(String nomePrint) throws IOException {

        File foto = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(foto, new File("out/prints/" + caminhoPrint + "/" + nomePrint + ".png"));

    }

    // Estrutura para leitura de dados de uma massa em formato CSV

    // Função de leitura do CSV
    private Iterator<Object[]> lerCSV(String nomeMassa) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(nomeMassa));
        ArrayList<Object[]> dados = new ArrayList<Object[]>();
        String linha;
        while ((linha = leitor.readLine()) != null) {
            String[] campos = linha.split(";");
            dados.add(campos);
        }
        leitor.close();
        return dados.iterator();
    }

    // Alimentar o DataProvider do Testng com os dados do CSV

    @DataProvider(name = "massaTeste")
    public Iterator<Object[]> massaTeste() throws IOException {
        return lerCSV("dataBase/massa.csv");
    }

    @BeforeMethod() // Executa antes de qualquer @Test dessa classe
    public void iniciar() throws IOException {
        url = "https://www.iterasys.com.br";
        System.setProperty("webdriver.chrome.driver", "Drivers/chromedriver/chromedriver.exe");
        System.setProperty("webdriver.edge.driver", "Drivers/edgedriver/msedgedriver.exe");
        System.setProperty("webdriver.ie.driver", "Drivers/iedriver/IEDriverServer.exe");
        System.setProperty("webdriver.gecko.driver", "Drivers/ffdriver/geckodriver.exe");
    }

    @Test(priority = 1, dataProvider = "massaTeste")
    public void consultarCurso(
            String id,
            String termo,
            String titulo,
            String preco,
            String browser) throws IOException, InterruptedException {

        // iniciar o browser
        switch (browser) {
            case "chrome":
                driver = new ChromeDriver(); // Instancia o Selenium como ChromeDriver
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "ie":
                driver = new InternetExplorerDriver();
                break;

        }


        // Configuração geral do browser
        driver.manage().timeouts().implicitlyWait(60000, TimeUnit.MILLISECONDS);
        driver.manage().window().maximize();


        driver.get(url); // Abre o navegador no endereço que consta na url

        print(id + "\\Consultar Curso - Passo 1 - Abriu o site");
        System.out.println("Abriu o browser");

        // Escrever o nome do curso na caixa de consulta

        driver.findElement(By.id("searchtext")).click();
        driver.findElement(By.id("searchtext")).clear();
        driver.findElement(By.id("searchtext")).sendKeys((termo));
           print(id + "\\Consultar Curso - Passo 2 - Consultou pelo curso " + titulo);
        driver.findElement(By.id("btn_form_search")).click(); // clica no botão com o icone da Lupa

        // Clicar no botão comprar do curso desejado
        //try {
            Assert.assertEquals( titulo ,driver.findElement(By.className("col-md-6")).getText());
              driver.findElement(By.cssSelector("span.comprar")).click();
              print(id + "\\Consultar Curso - Passo 3 - Visualiza o curso " + titulo + " entre os resultados");


            Assert.assertEquals(titulo, driver.findElement(By.cssSelector("span.item-title")).getText());
            Assert.assertEquals(preco, driver.findElement(By.cssSelector("span.new-price")).getText());
            print(id + "\\Consultar Curso - Passo 4 - Exibiu a página de compra do curso " + titulo);
            System.out.println("Localizar o curso");

       // } catch (Error | IOException e) {
            //Assert.assertEquals(" Desculpe não encontramos o curso que procura =( &nbsp;"  , driver.findElement(By.cssSelector("fa fa-warning")).getText());

          //  print(id + "\\Não encontrei o Curso");
            //System.out.println("Ítem Não Encontrado");

        }
   // }

    @AfterMethod // Executa depois de qualquer @Test dessa classe
    public void finalizar() {
        driver.quit();
        System.out.println("Fechar o browser");
    }
}












