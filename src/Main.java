import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;


public class Main {
	
	private static Integer keyContinue = 0;
	private static Integer k=3;//sigling level..
	private static double jc_threshold=0.75;
	private static HashMap<Integer, String> store = new HashMap<Integer,String>();
	private static Integer count_sentense =0;
	private static ArrayList<ArrayList<Integer>> sigled_sentenses = new ArrayList<ArrayList<Integer>>();
	private static ArrayList<String> coupled_sentenses1 = new ArrayList<String>();
	private static ArrayList<String> coupled_sentenses2 = new ArrayList<String>();
	private static ArrayList<String> coupled_sentenses3 = new ArrayList<String>();
	
	
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		file_read();
		//System.out.println(store);
		minhah_table(1);
		minhah_table(2);
		minhah_table(3);
		//System.out.println("sigled sentenses :"+sigled_sentenses);
		//System.out.println("coupled sentenses1:"+coupled_sentenses1);
		//System.out.println("coupled sentenses2:"+coupled_sentenses2);
		//System.out.println("coupled sentenses3:"+coupled_sentenses3);
		intersection();
		union();
		long endTime = System.nanoTime();
		double time_s = (endTime - startTime)/1000000000.0;
		System.out.println("runing time of program :"+time_s+ " s"); 
		//System.out.println(chk_wd(0, 2));
	}
	public static void file_read() {
		BufferedReader br = null;
		 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("src//data.txt"));
 
			while ((sCurrentLine = br.readLine()) != null) {
				String sen[] =sCurrentLine.split(" ");
				Shingling(sen);
				
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	
	}
	private static void Shingling(String[] sen_2_wd) {
		
		String line ="";
		if(sen_2_wd.length>=3){
			for (int i = 0; i <sen_2_wd.length-k+1; i++) {
				for (int j = i; j < i+k; j++) {
					line +=sen_2_wd[j];
				}
				line += " ";
			}
		}else{
			if(sen_2_wd.length==2)line =sen_2_wd[0]+sen_2_wd[1]; 
			if(sen_2_wd.length==1)line =sen_2_wd[0];
		}
		hashing(line);
		
		
	}	
	public static void hashing(String sh_sentense) {	
		
		String st[] =sh_sentense.split(" ");
		Integer uniqueId=keyContinue;
		

        for(int i=0;i<st.length;i++)
        {
            if(!store.values().contains(st[i])){
                store.put(uniqueId, st[i] );
                uniqueId = uniqueId+1;
            }                               
         }
        keyContinue = uniqueId;
        sen_show_hash(st);
	}
	
	public static void sen_show_hash(String st[]) {	
			int out[] = new int[st.length];	
			for (int i = 0; i < st.length; i++) {
				for (Entry<Integer, String> entry : store.entrySet()) {
			        if (st[i].equals(entry.getValue())) {
			            out[i] = entry.getKey();
			        }
			    }
			}
			sig_sentense_to_array(out);
	}
	
	public static void sig_sentense_to_array(int []a) {
		sigled_sentenses.add(new ArrayList<Integer>());
			for (int i = 0; i < a.length; i++) {
				sigled_sentenses.get(count_sentense).add(a[i]);
			}
		count_sentense++;
	}
		
	public static int hash_func(int a,int b) {
		int c=0 ;
		switch (a) {
		case 1:
			c=(b+1)%8;
			break;
		case 2:
			c=(b+2)%8;
			break;
		case 3:
			c=(2*b+1)%8;
			break;
		case 4:
			c=(2*b+2)%8;
			break;
		case 5:
			c=(3*b+1)%8;
			break;
		case 6:
			c=(3*b+2)%8;
			break;
		case 7:
			c=(4*b+1)%8;
			break;
		case 8:
			c=(4*b+2)%8;
			break;
		default:
			break;
		}
		return c;
		
	}
	
	public static void minhah_table(int ha_ty) {
		int [][]minhash_tb = new int[8][count_sentense+9];
		
		for (int f = 0; f < minhash_tb.length; f++) {
			for (int i = 1; i < minhash_tb[0].length-8; i++) {
				minhash_tb[f][i]= 3;
			}
		}
		
			for (int i = 0; i < minhash_tb.length; i++) {
				minhash_tb[i][0]=i;
			}
		
		
		
		
		
		for (int i = 0; i < minhash_tb.length; i++) {
			for (int j = count_sentense+1; j <= count_sentense+8; j++) {
				minhash_tb[i][j]=hash_func(j-count_sentense,minhash_tb[i][0]);
			}
		}
		
		
		for (int i = 0; i < minhash_tb.length; i++) {
				for (int j = 1; j < minhash_tb[i].length-8; j++) {
					if (chk_wd(j-1,minhash_tb[i][0]%8,ha_ty)) minhash_tb[i][j]=1;
				}
		}		
		
		System.out.println("minhash table .....");
		for (int i = 0; i < minhash_tb.length; i++) {
			for (int j = 0; j < minhash_tb[0].length; j++) {
				System.out.print(minhash_tb[i][j]+" ");
			}
			System.out.println();
		}
		minhash_signature(minhash_tb,ha_ty);
		
	}
	public static boolean chk_wd(int sent, int val,int ha_ty) {
		boolean ch = false;
		if(ha_ty==1){
			for (int i = 0; i < sigled_sentenses.get(sent).size(); i++) {
				if (sigled_sentenses.get(sent).get(i)==val)ch = true;
			}
		}
		if(ha_ty==2){
			for (int i = 0; i < sigled_sentenses.get(sent).size(); i++) {
				if (sigled_sentenses.get(sent).get(i)==(val+1)%8)ch = true;
			}
		}
		if(ha_ty==3){
			for (int i = 0; i < sigled_sentenses.get(sent).size(); i++) {
				if (sigled_sentenses.get(sent).get(i)==(val+5)%8)ch = true;
			}
		}
		return ch;
	}

	public static void minhash_signature(int[][]minhash_tb,int hy_ty) {
		int minhash_sig[][]= new int [8][count_sentense];
		for (int i = 0; i < minhash_sig.length; i++) {
			for (int j = 0; j < minhash_sig[0].length; j++) {
				minhash_sig[i][j]=9;
			}
		}		
		for (int i = 0; i < minhash_tb.length; i++) {
			for (int j = 1; j <= count_sentense; j++) {
				for (int l = 0; l < 8; l++) {
					if (minhash_tb[i][j]==1 && minhash_tb[i][minhash_tb[i].length-8+l]<minhash_sig[l][j-1] ){
						minhash_sig[l][j-1]= minhash_tb[i][minhash_tb[i].length-8+l];
					}
				}
			}
		}
		
		
		for (int i = 0; i < minhash_sig[0].length; i++) {
			for (int j = i+1; j < minhash_sig[0].length; j++) {
				jac_sim(i,j, minhash_sig,hy_ty);
			}
		}
		
		
		
		
		/*for (int i = 0; i < minhash_sig[0].length; i++) {
			for (int j = 0; j < minhash_sig[0].length; j++) {
				if(
				i!=j && 
				minhash_sig[0][i] == minhash_sig[0][j] && minhash_sig[0][i] != 9 &&
				minhash_sig[1][i] == minhash_sig[1][j] && minhash_sig[1][i] != 9 &&
				minhash_sig[2][i] == minhash_sig[2][j] && minhash_sig[2][i] != 9 &&
				minhash_sig[3][i] == minhash_sig[3][j] && minhash_sig[3][i] != 9 &&
				minhash_sig[4][i] == minhash_sig[4][j] && minhash_sig[4][i] != 9 &&
				minhash_sig[5][i] == minhash_sig[5][j] && minhash_sig[5][i] != 9 &&
				minhash_sig[6][i] == minhash_sig[6][j] && minhash_sig[6][i] != 9 &&
				minhash_sig[7][i] == minhash_sig[7][j] && minhash_sig[7][i] != 9 
				)couples(i,j);
			}
		}*/
		
		System.out.println("minhash signature....");
		for (int i = 0; i < minhash_sig.length; i++) {
			for (int j = 0; j < minhash_sig[0].length; j++) {
				System.out.print(minhash_sig[i][j]+" ");
			}
			System.out.println();
		}
		
	}
	public static void couples(int s1,int s2, int hy_ty) {
		
		int ss1 = 1+Math.min(s1, s2);
		int ss2 = 1+Math.max(s1, s2);
		String couple_sen = ss1+" "+ss2;
		//System.out.println(couple_sen);
		if(hy_ty==1){
		coupled_sentenses1.add(couple_sen);
		removeDuplicate(coupled_sentenses1);
		}
		if(hy_ty==2){
			coupled_sentenses2.add(couple_sen);
			removeDuplicate(coupled_sentenses2);
		}
		if(hy_ty==3){
			coupled_sentenses3.add(couple_sen);
			removeDuplicate(coupled_sentenses3);
		}
	}

	public static void removeDuplicate(ArrayList<String> arlList)
	  {
	   HashSet<String> h = new HashSet<String>(arlList);
	   arlList.clear();
	   arlList.addAll(h);
	  }

	public static void jac_sim(int s_check1, int s_check2,int [][]minhash_sig,int hy_ty) {
		
		double both_hv= 0;
		double jc_val = 0;
		for (int i = 0; i < 8; i++) {
			if(minhash_sig[i][s_check1]==minhash_sig[i][s_check2] && minhash_sig[i][s_check1]!=9)both_hv++;
		}
		jc_val=both_hv/8;
		if(jc_threshold<=jc_val){
			couples(s_check1,s_check2,hy_ty);
		}
		//System.out.println("jc threshold:"+"s-"+(s_check1+1)+","+(s_check2+1)+":"+jc_val);
		
	}
	
	public static void intersection() {
        
		 List<String> list1 = new ArrayList<String>(coupled_sentenses1);
		 List<String> list2 = new ArrayList<String>(coupled_sentenses2);
		 List<String> list3 = new ArrayList<String>(coupled_sentenses3);
		 List<String> list11 = new ArrayList<String>();
		 List<String> list_fin = new ArrayList<String>();
		 

	        for (String t : list1) {
	            if(list2.contains(t)) {
	                list11.add(t);
	            }
	        }
	        for (String t : list11) {
	            if(list3.contains(t)) {
	                list_fin.add(t);
	            }
	        }

        System.out.println("most similar pairs:"+list_fin);
        System.out.println("most similar pairs:"+list_fin.size());
    }

	public static void union() {
        Set<String> set = new HashSet<String>();
        set.addAll(coupled_sentenses1);
        set.addAll(coupled_sentenses2);
        set.addAll(coupled_sentenses3);

        System.out.println("may similar pairs:"+set);
        System.out.println("may similar pairs:"+set.size());
        print_output(set);
    }
	
	public static void print_output(Set<String> set) {
		try {
			 
			String[] content =set.toArray(new String[0]);
			
 
			File file = new File("src/scs2009_sentences.out");
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String newLine = System.getProperty("line.separator");
			
			for (int i = 0; i < set.size(); i++) {
				bw.write(content[i]+newLine);
				
			}
			
			//bw.write(content);
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
