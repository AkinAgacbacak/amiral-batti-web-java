import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class AmiralBatti {

    final public static int GEMI_SAYISI=2;

    public static void tableReset(String[][] table){
        for(int i=0;i<table.length;i++){
            for (int j=0; j<table[i].length;j++){
                if (i == 0 && j == 0) {
                    table[i][j] = "X";
                }
                else if (i == 0) {
                    // Karakteri hesaplayıp String'e çeviriyoruz
                    table[i][j] = String.valueOf((char) ('A' + j - 1));
                }
                else if (i != 0 && j == 0) {
                    // Sayıyı String'e çeviriyoruz
                    table[i][j] = String.valueOf(i);
                }
                else
                    table[i][j]= "~";
            }
        }
    }

    public static void Displaytable(String[][] table){
        for(int i=0;i<table.length;i++){
            for (int j=0; j<table[i].length;j++){
                System.out.print(table[i][j]+"  ");
            }
            System.out.println();
        }
    }

    public static String[] GetAmiral(String[][] tablePlayer){
        Scanner scanner = new Scanner(System.in);
        String[] playerBotCoordinates = new String[GEMI_SAYISI];
        System.out.println("20 kare Gemi hakkınız var.\n Gemilerin konuşlanacağı kordinatları Tek tek veriniz.(Örnek: D3) ");
        for(int i=0; i<GEMI_SAYISI;i++){
            System.out.print(i+1+". Gemi Kordinatını Giriniz: ");
            String coordinate = scanner.nextLine();
            getKordinat(coordinate, tablePlayer);
            playerBotCoordinates[i]=coordinate;
        }
        return playerBotCoordinates;
    }

    public static void getKordinat(String kordinat, String[][] table){
        int column = kordinat.charAt(0) - 'A' + 1;
        int row = Character.getNumericValue(kordinat.charAt(1));

        table[row][column]="#";
    }

    public static String randomCreateCoordinat(){
        Random rand = new Random();
        char coordinatesRows = (char)('A' + Math.random() * ('J' - 'A' + 1));
        int coordinatesColumn = rand.nextInt(10)+1 ;
        String coordinate = coordinatesRows+String.valueOf(coordinatesColumn);

        return coordinate;
    }

    public static String[] CreateBotTable (String[][] tableBot){
        String[] botcoordinates = new String[GEMI_SAYISI];

        for(int i=0; i<GEMI_SAYISI; i++){
        String coordinate = randomCreateCoordinat();
        System.out.println(coordinate);

        botcoordinates[i]= coordinate;
        getKordinat(coordinate,tableBot);
        }

        return botcoordinates;
    }

    public static boolean isShoot(String[] Coordinates,String ShootCoordinate ){
        for(int i=0;i<Coordinates.length;i++){
            if (Coordinates[i].equals(ShootCoordinate))
                return true;
        }
        return false;
    }

    public static void shootCoordinate(String[][] table,String coordinate,boolean isShoot){
        int column = coordinate.charAt(0) - 'A' + 1;
        int row = Character.getNumericValue(coordinate.charAt(1));

        if (isShoot)
            table[row][column]="X";
        else
            table[row][column]=".";

    }

    public static String getSmartRandomCoordinate(java.util.ArrayList<String> history) {
        String newCoordinate;
        do {
            newCoordinate = randomCreateCoordinat(); // Mevcut rastgele üreticiyi çağır
        } while (history.contains(newCoordinate)); // Eğer listede varsa tekrar üret

        history.add(newCoordinate); // Benzersiz koordinatı listeye ekle
        return newCoordinate;
    }


    public static int countShot(String[][] table) {
        int counter = 0;

        // Satırları gez
        for (int i = 0; i < table.length; i++) {
            // Sütunları gez
            for (int j = 0; j < table[i].length; j++) {
                // Eğer hücre null değilse ve "X" değerine eşitse
                if (table[i][j] != null && table[i][j].equals("X")) {
                    counter++;
                }
            }
        }
        return counter;
    }


    static void main() {
        Scanner scan = new Scanner(System.in);
        String[][] tablePlayer = new String[11][11]; // Table aslında 10-10 luk bi tahta üstünde oynanır ama 0 indixli olanları
        String[][] tableBot    = new String[11][11]; // eksen çizgileri olarak kullanıcam.
        String[][] tableEmpty    = new String[11][11];

        tableReset(tableBot);
        tableReset(tablePlayer);
        tableReset(tableEmpty);

        System.out.println("======AMİRAL-BATTI=======\n\n");
        System.out.println("Oyuncu Denizi");
        System.out.println("_______________________________");
        Displaytable(tablePlayer);

        System.out.println("\nHadi Gemilerinizi Yerleştirelim.");
        String[] playerCoordinats = GetAmiral(tablePlayer);
        Displaytable(tablePlayer);
        System.out.println("_______________________________");

        System.out.println("Bot Gemilerini yerleştiriyor...\n\n");
        String[] botcoordinates = CreateBotTable(tableBot);

        System.out.println("Bot Denizi ");
        System.out.println("_______________________________");

        Displaytable(tableEmpty);

        int numberOfShotsMadeByPlayer = 0 , numberOfShotsMadeByBot = 0 ;
        boolean playerShootingPermit = true; //İlk atışı player yapacağı için true
        boolean botShootingPermit = false;
        boolean isBotShoot=false , isPlayerShoot=false;

        String playerShootCoordinate="" , botShootCoordinate="";


        System.out.println("Oyun Başlasın İlk Atışı Yap.");

        ArrayList<String> botShotHistory = new ArrayList<>();

        do{
            if(playerShootingPermit) {
                 System.out.print("Atış Kordinatı: ");
                 playerShootCoordinate = scan.nextLine();
                 isBotShoot = isShoot(botcoordinates,playerShootCoordinate);
                 System.out.println(isBotShoot? "Amiral Battı!": "Iskaladın!");
                 shootCoordinate(tableEmpty,playerShootCoordinate,isBotShoot);

                 System.out.println("Bot Denizi ");
                 System.out.println("_______________________________");
                 Displaytable(tableEmpty);

                 numberOfShotsMadeByPlayer++;
                 playerShootingPermit = isBotShoot;
                 botShootingPermit = !isBotShoot;
            }
            else if (botShootingPermit) {
                System.out.println("Bot Atış Kordinatını giriyor... ");
                botShootCoordinate = getSmartRandomCoordinate(botShotHistory);

                System.out.printf("Bot Atış Yaptığı kordinat: %s\n",botShootCoordinate);
                isPlayerShoot = isShoot(playerCoordinats,botShootCoordinate);

                System.out.println(isPlayerShoot? "Amiral Battı!": "Iskaladın!");
                shootCoordinate(tablePlayer,botShootCoordinate,isPlayerShoot);

                System.out.println("Oyuncu Denizi");
                System.out.println("_______________________________");
                Displaytable(tablePlayer);

                numberOfShotsMadeByBot++;
                botShootingPermit = isPlayerShoot;
                playerShootingPermit = !isPlayerShoot;
            }

            System.out.println(countShot(tablePlayer)+"  "+countShot(tableEmpty));
        }while(countShot(tablePlayer)-1 < GEMI_SAYISI && countShot(tableEmpty)-1 < GEMI_SAYISI); //-1 YAPMA NEDENİM BİR KEZ HER TABLODA X GEÇMESİ

        if (countShot(tablePlayer)-1==GEMI_SAYISI){
            System.out.println("Bot kazandı ");
        }
        else if (countShot(tableEmpty)-1==GEMI_SAYISI)
            System.out.println("Player kazandı");



    }
}
