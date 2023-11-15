import java.util.Arrays;
import java.util.ArrayList;

public class PosXVisitor implements Visitor{
    private static String[] positiveWords = {
        "adorable", "amazing", "awesome", "angelic", "acclaimed", 
        "affirmative", "agreeable", "appealing", "attractive", "beaming",
        "beautiful", "beneficial", "best", "better", "bliss", 
        "brave", "bravo", "brilliant", "calm", "celebrate", 
        "celebratory", "champ", "champion", "cheer", "cheery", 
        "cheerful", "congrats", "congratulations", "cool", "courageous", 
        "creative", "cute", "dazzling", "delight", "delightful", 
        "eager", "earnest", "ecstatic", "effective", "efficient", 
        "effortless", "elegant", "enchanting", "encouraging", "energetic", 
        "energized", "engaging", "enthusiastic", "exalt", "excellent", 
        "excel", "exciting", "exquisite", "fabulous", "fair", 
        "fantastic", "favorable", "flavorful", "flourishing", "fortunate", 
        "fresh", "friendly", "fun", "funny", "generous", 
        "genius", "giving", "glamorous", "good", "gorgeous", 
        "graceful", "great", "healthy", "heavenly", "honest", 
        "honor", "honorable", "honored", "hug", "ideal", 
        "imaginative", "impress", "impressive", "innovate", "innovative",
        "intellectual", "intelligent", "intuitive", "inventive", "jovial",
        "joy", "joyous", "joyful", "jubilant", "keen",
        "kind", "kindly", "knowledgable", "laugh", "legendary", 
        "lively", "lit", "lovely", "lucky", "marvelous",
        "masterful", "mastery", "meaningful", "merit", "miracle",
        "miraculous", "motivate", "motivating", "nice", "nutritious", 
        "okay", "optimistic", "optimist", "paradise", "perfect", 
        "perfection", "perfectly", "phenomenal", "please", "pleasant",
        "pleasure", "pleasurable", "pog", "poggers", "polished", 
        "popular", "positive", "powerful", "pretty", "productive", 
        "progress", "proud", "quality", "reassuring", "refined", 
        "refine", "refreshing", "rejoice", "reliable", "remarkable",
        "resounding", "respect", "respected", "respectful", "responsible",
        "reward", "rewarding", "award", "awarding", "safe",
        "safety", "satisfactory", "secure", "skilled", "skillful",
        "smile", "smiling", "soulful", "special", "spiritual", 
        "stunning", "stupendous", "success", "successful", "superb",
        "surprising", "surprise", "terrific", "thrilling", "thriving",
        "tranquil", "tranquility", "trusting", "truthful", "upbeat",
        "unbelievable", "valued", "vibrant", "victorious", "victory",
        "virtuous", "vivacious", "welcome", "wholesome", "wonderful", 
        "yes", "yummy"
    };

    private static ArrayList<String> positiveWordSet = new ArrayList<>(Arrays.asList(positiveWords));
    @Override
    public boolean visitX(X xMessage){
        return findPositiveWords(xMessage);
    }

    private boolean findPositiveWords(X xMessage){
        String xContent = xMessage.getMessage();
        String[] tokens = xContent.split("[ .,!?:;'\"-]+\\s*");
        for (String token : tokens){
            if(positiveWordSet.contains(token.toLowerCase())){
                return true;
            }
        }
        return false;
    }
}