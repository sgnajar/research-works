package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Readxml {

	public void startUp()
	{
		String inputFile = "/Users/snajar/Documents/someRdpOut.txt";
		String outputFile = "/Users/snajar/Documents/someRDPout.xml";
		//String outputFile2 = "C:\\Users\\Sasan Najar\\Desktop\\output2.txt";
		String line = "";
		String splitBy = "\t";
		String beginPar = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--  An excerpt of an egocentric social network -->"
				+ "\n<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">\n<graph edgedefault=\"undirected\">"
				+ "\n\n<!-- data schema -->\n<key id=\"name\" for=\"node\" attr.name=\"name\" attr.type=\"string\"/>"
				+ "\n<key id=\"level\" for=\"node\" attr.name=\"gender\" attr.type=\"string\"/>"
				+ "\n\n<!-- nodes -->\n<node id=\"1\">\n <data key=\"name\">root</data>\n <data key=\"level\">root</data>"
				+ "\n</node>\n";
		int countLine = 0;
		int countNode = 2;

		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			//BufferedWriter bw2 = new BufferedWriter(new FileWriter(outputFile2));
			bw.write(beginPar);
			//br.readLine();
			//Set <String> myUniqueList = new LinkedHashSet<String>();
			ArrayList<String>mylevelsList = new ArrayList<String>();
			//Set <String> dups = new LinkedHashSet<String>();
			//HashMap <String, Integer> myHashMap = new LinkedHashMap <String, Integer>();
			Map<String,Integer> myNodeMap = new HashMap <String, Integer>();
			//myNodeMap.put("root",1);
			Map<Integer,Integer> mytargetMap = new HashMap <Integer, Integer>();
			
			if (line == null)
				throw new IOException("this file is empty");
				//System.out.println("this file is empty");
			while ((line = br.readLine()) != null) 
			{
				countLine++;
				if (countLine == 1)
				{
					String[] myTree = line.split(splitBy);
					for ( String s : myTree)
					{
						//myUniqueList.add(s);
						mylevelsList.add(s);
					}
				}
				else
				{
					String[] myTree = line.split(splitBy);
					for (int x=0; x< myTree.length; x++)
					{
						String temp = myTree[x];
						//myHashMap.put(myTree[x], x);
						if (temp.isEmpty())
						{
							//System.out.println(temp);
						}
						else
						{
							if (myNodeMap.containsKey(temp)){
								//System.out.println(myNodeMap.containsKey(temp));
							}
							else
							{
								myNodeMap.put(temp, countNode);
								countNode++;
								String mylevel = mylevelsList.get(x);

								bw.append("<node id=" + "\""+ myNodeMap.get(temp) +"\">\n");
								bw.append(" <data key=\"name\">" + temp + "</data>\n");
								bw.append(" <data key=\"level\">" + mylevel + "</data>\n");
								bw.append(" </node>\n");
								
								int nodeSrc = 1;
								if (x == 0)
								{
									mytargetMap.put(myNodeMap.get(temp), nodeSrc);
								}
								else
								{
									for (int y = (x - 1 ); y > -1; y--)
									{
										String temp2 = myTree[y];
										if (temp2.isEmpty())
										{
											continue;
										}
										else
										{
											nodeSrc = myNodeMap.get(temp2);
											mytargetMap.put(myNodeMap.get(temp), nodeSrc);
											break;
										}
									}
								}
							}
						}
					}
				}
			}

		br.close();
		bw.append("\n<!-- edges -->\n");
		
		Map<String, Integer> myNodeMapSort = getSortedNodes(myNodeMap);
		Map<Integer,String> revNode = new HashMap<Integer,String>();
		
		for (String k: myNodeMapSort.keySet())
		{
			//System.out.println(myNodeMapSort.get(k));
			//System.out.println(k);
			revNode.put(myNodeMapSort.get(k), k);
		}
		
		Map<Integer, Integer> mySortedTargetMap = getSortedEdges(mytargetMap);
		Set<Integer> myEdges = new HashSet<Integer>();
		
		for (Integer trgt: mySortedTargetMap.keySet())
		{
			//System.out.println(k2);
			int src = mySortedTargetMap.get(trgt);
			if (myEdges.contains(src))
			{
				bw.append("<edge source=\""+ src +"\" target=\""+ trgt + "\"></edge>\n");
			}
			else
			{
				myEdges.add(mySortedTargetMap.get(trgt));
				String theParent = revNode.get(trgt);
				bw.append("<!-- " + theParent +"'s children -->\n");
				bw.append("<edge source=\""+ src +"\" target=\""+ trgt + "\"></edge>\n");
			}
		}
		
		bw.append("\n</graph>\n</graphml>");
		bw.close();
		
		}catch (FileNotFoundException e){
			System.out.println("Unable to find the file");
		}catch (IOException e){
			System.out.println("Unable to read the file");
		}
	}

	Map<String, Integer> getSortedNodes(Map<String, Integer> m) 
	{
		Comparator<Map.Entry<String, Integer>> srtNode = new Comparator<Map.Entry<String, Integer>>()
		{
			public int compare(Map.Entry<String, Integer> m1, Map.Entry<String, Integer> m2)
			{
				return m1.getValue().compareTo(m2.getValue());
			}
		};
		
		List<Map.Entry<String, Integer>> newSrtNodeList = new ArrayList<Map.Entry<String, Integer>>();
		newSrtNodeList.addAll(m.entrySet());
		Collections.sort(newSrtNodeList, srtNode);
		Map<String, Integer> newSrtMap = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> x: newSrtNodeList)
		{
			newSrtMap.put(x.getKey(), x.getValue());
		}
		return newSrtMap;
	}

	Map<Integer, Integer> getSortedEdges(Map<Integer, Integer> m) 
	{
		Comparator<Map.Entry<Integer, Integer>> srtEdge = new Comparator<Map.Entry<Integer, Integer>>()
		{
			public int compare(Map.Entry<Integer, Integer> m1, Map.Entry<Integer, Integer> m2)
			{
				return m1.getValue().compareTo(m2.getValue());
			}
		};
		
		List<Map.Entry<Integer, Integer>> newSrtEdgeList = new ArrayList<Map.Entry<Integer, Integer>>();
		newSrtEdgeList.addAll(m.entrySet());
		Collections.sort(newSrtEdgeList, srtEdge);
		Map<Integer, Integer> newSrtMap = new LinkedHashMap<Integer, Integer>();
		for (Map.Entry<Integer, Integer> x: newSrtEdgeList)
		{
			newSrtMap.put(x.getKey(), x.getValue());
		}
		return newSrtMap;	
	}

	public static void main(String[] args) throws IOException 
	{
		Readxml rx = new Readxml();
		rx.startUp();
		System.out.println("The output file is ready to view.");
	}
}


