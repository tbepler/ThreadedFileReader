package wordfinder;
import java.util.*;

public class MapUtil
{
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
	{
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>( map.entrySet() );
		Collections.sort( 
				list, 
				new Comparator<Map.Entry<K, V>>() {
					public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
					{
						return (o1.getValue()).compareTo( o2.getValue() );
					}
				} );

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list)
		{
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> getEntriesByValue(Map<K, V> map, int num, boolean ascending){
		PriorityQueue<Map.Entry<K, V>> q;
		if(ascending){
			q = new PriorityQueue<Map.Entry<K, V>>(
					map.size(), 
					new Comparator<Map.Entry<K, V>>(){
						public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2){
							return (o1.getValue()).compareTo( o2.getValue() );
						}
					});
		}else{
			q = new PriorityQueue<Map.Entry<K, V>>(
					map.size(), 
					new Comparator<Map.Entry<K, V>>(){
						public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2){
							return (o2.getValue()).compareTo( o1.getValue() );
						}
					});
		}
		
		q.addAll(map.entrySet());
		
		Map<K, V> result = new LinkedHashMap<K, V>();
		while(!q.isEmpty() && result.size()<num){
			Map.Entry<K, V> entry = q.poll();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}