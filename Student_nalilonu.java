import java.util.List;
import java.util.PriorityQueue;
import java.util.Iterator;
import java.util.Comparator;

public class Student_nalilonu implements Student {

  private class Node {
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

  private class preferenceNode {
    private int schoolIndex;
    private Double preferenceScore;

    public preferenceNode(int schoolIndex, Double preferenceScore) {
      this.schoolIndex = schoolIndex;
      this.preferenceScore = preferenceScore;
    }

    public int getSchool() {
      return schoolIndex;
    }

    public Double getScore() {
      return preferenceScore;
    }

  }
  
  class NodeComparator implements Comparator<Node>{ 
    public int compare(Node n1, Node n2) { 
      if (n1.getSynergy() < n2.getSynergy()) 
          return 1; 
      else if (n1.getSynergy() > n2.getSynergy()) 
          return -1; 
      else
        return 0; 
    }
  } 
  
  class PreferenceNodeComparator implements Comparator<preferenceNode>{ 
    public int compare(preferenceNode n1, preferenceNode n2) { 
      if (n1.getScore() < n2.getScore()) 
          return 1; 
      else if (n1.getScore() > n2.getScore()) 
          return -1; 
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
      PriorityQueue<preferenceNode> preferenceQueue = new PriorityQueue<preferenceNode>(new PreferenceNodeComparator());

      for (int i = 0; i < N; i++) {
        // Calculate quality + synergy.
        Double preferenceScore = schools.get(i) + synergies.get(i);

        // Put this score in a node.
        preferenceNode node = new preferenceNode(i, preferenceScore);
        
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
      int[] applications = new int[10];
      int index = 0; // counter to keep track of how many schools we have inside applications array

      // calculate alpha as the number of schools we want to apply to from our true preferences
      double ratio = aptitude / S;
      int alpha = (int) Math.ceil(ratio * 10);

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
      while (index < 10) {
        Node nextSchool = waitlist.poll();
        applications[index] = nextSchool.getSchool();
        index++;
      }
      //return the applications array  
      return applications;

    }

    public static void main(String args[]) {
      
    }
}