import java.util.List;
import java.util.PriorityQueue;
import java.util.Iterator;
import java.util.Comparator;
import java.util.*;

public class Student_nalilonu implements Student {

  private static class Node {
    private Double synergy;
    private int schoolIndex;

    public Node(int newSchoolIndex, Double newSynergy) {
      synergy = newSynergy;
      schoolIndex = newSchoolIndex;
    }

    public Double getSynergy() {
      return synergy;
    }

    public int getSchool() {
      return schoolIndex;
    }

  }

  private class PreferenceNode {
    private int schoolIndex;
    private Double preferenceScore;

    public PreferenceNode(int schoolIndex, Double preferenceScore) {
      this.schoolIndex = schoolIndex;
      this.preferenceScore = preferenceScore;
    }

    public int getSchool() {
      return schoolIndex;
    }

    public double getScore() {
      return preferenceScore;
    }

  }
  
  public static class NodeComparator implements Comparator<Node>{ 
    public int compare(Node n1, Node n2) { 
      if (n1.getSynergy() < n2.getSynergy()) 
          return 1; 
      else if (n1.getSynergy() > n2.getSynergy()) 
          return -1; 
      else
        return 0; 
    }
  } 
  
  public static class PreferenceNodeComparator implements Comparator<PreferenceNode>{ 
    public int compare(PreferenceNode n1, PreferenceNode n2) { 
      if (n1.getScore() < n2.getScore()) 
          return -1; 
      else if (n1.getScore() > n2.getScore()) 
          return 1; 
      else
        return 0; 
    }
  } 
  
  public int[] getApplications(
    int N,
    double S,
    double T,
    double W,
    double aptitude,
    List<Double> schools,
    List<Double> synergies) {

     
  
      // Calculate the true preferences array
      //------------------------------------------------------------------------------
      int[] preferences = new int[N];
      PriorityQueue<PreferenceNode> preferenceQueue = new PriorityQueue<PreferenceNode>(new PreferenceNodeComparator());

      for (int i = 0; i < N; i++) {
        // Calculate quality + synergy.
        Double preferenceScore = schools.get(i) + synergies.get(i);

        // Put this score in a node.
        PreferenceNode node = new PreferenceNode(i, preferenceScore);
        
        // Put node in a priority queue.   
        preferenceQueue.add(node);
      }

      // Extract one by one and put in an array for preferences.
      int k = 0;
      while (!preferenceQueue.isEmpty()) {
        preferences[k] = preferenceQueue.poll().getSchool();
        k++;
      }

      // Reverse array (since elements are retrieved in increasing order of score).
      int[] newPreferences = new int[N]; 
      int j = N; 
      for (int i = 0; i < N; i++) { 
          newPreferences[j - 1] = preferences[i]; 
          j = j - 1; 
      } 
      preferences = newPreferences;
      //------------------------------------------------------------------------------

      // create an array of size 10 to store the schools we want to apply to
      int schoolNum = 10;
      int[] applications = new int[10];
      int index = 0; // counter to keep track of how many schools we have inside applications array
      
      // the case for if max synergy is 0 -> random choice
      if (W == 0) {
        // create a boolean array of length number of schools where all values are initiated to false (the indices of the array represents the school)
        boolean[] applied = new boolean[N];
        // we want a while loop that keeps generating a floor(Math.random) * number of schools
        while (index < schoolNum) {
          // when we pick a school by random, we check the array to see if we've applied yet
          // if no, check the value at that index to be true and increment index
          int randomSchool = (int) Math.floor(Math.random()*N);
          if (!applied[randomSchool]) { // we did not already apply here
            applied[randomSchool] = true;
            applications[index] = randomSchool;
            index++;
          }
          // if yes, go to the next iteration of the while loop
          
        }
      }

      // calculate alpha as the number of schools we want to apply to from our true preferences
      
      else { // (W != 0)
        double maxAptitude = S;
        if (S == 0) {
          maxAptitude = 1;
        }
        double ratio = aptitude / maxAptitude;
        int alpha = (int) Math.ceil(ratio * schoolNum);

        //for loop with i <= alpha. go through our preferences from the beginning and add each school we hit to the application array
        for (int i = 0; i < alpha; i++) {
          applications[index] = preferences[i];
          index++;
        }

        // create the comparator
        NodeComparator compareSchools = new NodeComparator();

        //create a priority queue
        PriorityQueue<Node> waitlist = new PriorityQueue<Node>(compareSchools);

        // use another for loop and add each remaining school into a priority queue 
        // with key being the school and the value being the synergy
        for (int i = alpha; i < preferences.length; i++) {
          int schoolIndex = preferences[i];
          Node newNode = new Node(schoolIndex, synergies.get(schoolIndex));
          waitlist.add(newNode);
        }

        // use a while loop with the condition (counter < 10) and take out 10 - alpha schools from the priority queue and put them into the application array
        while (index < schoolNum) {
          Node nextSchool = waitlist.poll();
          applications[index] = nextSchool.getSchool();
          index++;
        }
      }
      
      //return the applications array  
      return applications;

    }

    public static void main(String args[]) {
      // test priority queue to make sure comparator is working as intended
      
      NodeComparator comparator = new NodeComparator();
      PriorityQueue<Node> test = new PriorityQueue<Node>(comparator);
      Node node1 = new Node(1, 2.0);
      Node node2 = new Node(2, 4.0);
      Node node3 = new Node(3, 3.0);
      test.add(node1);
      test.add(node2);
      test.add(node3);
      
      System.out.println(test.peek().getSynergy()); 
      System.out.println(test.poll().getSchool());

      System.out.println(test.poll().getSynergy());

      System.out.println(test.poll().getSynergy());

      // create schools to test getApplications
      List<Double> schoolQualities = new ArrayList<Double>(Arrays.asList(30.0, 2.0, 45.0, 3.0, 50.0, 2.0, 40.0, 1.0, 43.0, 2.0, 0.3));
      List<Double> schoolSynergies = new ArrayList<Double>(Arrays.asList(6.0,  2.0,  9.0, 3.0, 10.0, 2.0,  7.0, 1.0,  8.0, 2.0, 0.3));
      // List<Double> schoolSynergies = new ArrayList<Double>(Arrays.asList(0.0,  0.0,  0.0, 0.0, 0.0, 0.0,  0.0, 0.0,  0.0, 0.0, 0.0));


      double maxSynergy = 10.0;
      double maxQuality = 50.0;
      double ourAptitude =  5.0; // change these for different test cases
      double maxAptitude =  10.0; // change these for different test cases
      
      Student testStudent = new Student_nalilonu();
      int[] apps = testStudent.getApplications(schoolQualities.size(), maxAptitude, maxQuality, maxSynergy, ourAptitude, schoolQualities, schoolSynergies);

      System.out.println("Here are the apps: ");
       for (int j = 0; j < apps.length; j++) {
         System.out.println(apps[j]);
       }

    }
}