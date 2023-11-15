import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Feed{
    Map<Integer, X> XMap;

    public Feed(){
        XMap = new HashMap<>();
    }

    public void addToFeed(X xMessage){
        int xId = xMessage.getXID();

        if(!XMap.containsKey(xId)){
            XMap.put(xId, xMessage);
        }
    }

    public void mergeFeed(Feed feed){
        this.XMap = Stream.of(this.XMap, feed.getXMap())
        .flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(v1,v2) -> new X(v2.getAuthor(), v2.getMessage())));
    }

    public Map<Integer, X> getXMap(){
        return XMap;
    }

    public List<X> getRevOrdTweetList(){
        List<X> XList = new ArrayList<>();
        PriorityQueue<Map.Entry<Integer, X>> maxHeap = new PriorityQueue<>((a,b) -> b.getKey() - a.getKey());

        for(Map.Entry<Integer, X> entry : XMap.entrySet()){
            maxHeap.offer(entry);
        }

        while(!maxHeap.isEmpty()){
            XList.add(maxHeap.poll().getValue());
        }
        return XList;
    }
}