package com.example.gymmembershipgui;

/**
 * MemberDatabase manages the members at a gym, or in a fitness class.
 * Each database is defined with an array of members as well as a size of the
 * database. Members can be found in the database by comparing each member in
 * the database to the member that needs to be found. Additionally, when the
 * database has reached capacity and more members need to be added, the
 * database can grow in size. If a member wants to join the gym, they can be
 * added to the database, and if a member would like to leave, they can be
 * removed. The database can also sort the members by name, location,
 * expiration date, fee and display the members in the database.
 * @author Arunima Tripathy, Mahfuza Rahman
 */
public class MemberDatabase {
    private Member[] mlist;
    private int size;
    private final static int NOT_FOUND = -1;
    private final static int INITIAL_LENGTH = 4;
    private final static int INITIAL_SIZE = 0;

    /**
     * Creates an instance of MemberDatabase with a list of members and size.
     * Creates an array of length four and sets the size of database to 0.
     * The size represents the number of members in the array and the index
     * the next member should be added to.
     */
    public MemberDatabase() {
        mlist = new Member[INITIAL_LENGTH];
        size = INITIAL_SIZE;
    }

    /**
     * Finds the index number at which a member exists in the list of members.
     * Iterates through array of members, and checks if an existing member
     * equals the member passed as a parameter.
     * @param member the member that must be found.
     * @return the index the member is at in list of members, -1 if not found.
     */
    private int find(Member member) {
        for (int i = 0; i < size; i++) {
            if (mlist[i].equals(member))
                return i;
        }
        return NOT_FOUND;
    }

    /**
     * Increases the size of database when the array is filled to capacity.
     * Creates a new array with the length of the list of members plus 4.
     * Copies the members of the original array of members to the new array.
     * Sets the old array of members to equal to the new array of members.
     */
    private void grow() {
        // create new member list of increased size to copy old member list
        Member[] tempMList = new Member[mlist.length + 4];
        for (int i = 0; i < size; i++) {
            tempMList[i] = mlist[i];
        }
        mlist = tempMList;
    }

    /**
     * Adds a member to the database.
     * If array of members is filled, database is grown. Checks if member
     * already exists in the database. If member does not exist, then the
     * member is added, and size is grown. Otherwise, member is not added.
     * @param member the member that is being added to the database.
     * @return true if member is added, false if member in database already.
     */
    public boolean add(Member member) {
        if (size == mlist.length)
            grow();
        if(find(member) != NOT_FOUND)
            return false;
        mlist[size++] = member;
        return true;
    }

    /**
     * Removes a member from the database.
     * Checks if member exists in the database. If member exists, the member
     * is removed from the list of members, and following members are moved to
     * left of list, and size is decreased.
     * @param member the member that must be removed.
     * @return true if member removed, false if the member is not in database.
     * */
    public boolean remove(Member member) {
        int removalIndex = find(member); // find index of member to remove
        if (removalIndex == NOT_FOUND)
            return false; // if the member is not in the list, invalid removal
        if(removalIndex == size - 1) { // if member at end of list, remove end
            mlist[removalIndex] = null;
            size--;
            return true;
        }
        // to maintain order of members in list, move each member to the left
        for(int i = removalIndex; i < size - 1; i++)
            mlist[i] = mlist[i+1];
        mlist[--size] = null;
        return true;
    }

    /**
     * Displays the list of members in database.
     * Iterates through the list of members and prints each member.
     */
    public void print() {
        System.out.println("\n-list of members-");
        for(int i = 0; i < size; i++)
            System.out.println(mlist[i].toString());
        System.out.println("-end of list-\n");
    }

    /**
     * Displays the list of members in database ordered by county and zipcode.
     * Sorts the list by county and zip code using in-place insertion sort.
     * Iterates through the list of members and prints each member.
     */
    public void printByCounty() {
        for(int i = 1; i < size; i++){
            Member keyMember = mlist[i];
            int j = i - 1;
            // Move each location alphabetically ahead of key location up
            while(j >= 0 && mlist[j].getLocation().ordinal() >
                    keyMember.getLocation().ordinal())
                mlist[j+1] = mlist[j--];
            mlist[j + 1] = keyMember;
        }
        System.out.println("\n-list of members sorted by county and " +
                "zipcode-");
        for(int i = 0; i < size; i++)
            System.out.println(mlist[i].toString());
        System.out.println("-end of list-\n");
    }

    /**
     * Displays the list of members in database ordered by expiration date.
     * Sorts the list by membership expiration using in-place insertion sort.
     * Iterates through the list of members and prints each member.
     */
    public void printByExpirationDate() {
        for(int i = 1; i < size; i++){
            Member keyMember = mlist[i];
            int j = i - 1;
            // Move each member that has a future expiration date up
            while(j >= 0 &&
                    mlist[j].getExpire().compareTo(keyMember.getExpire()) > 0)
                mlist[j+1] = mlist[j--];
            mlist[j + 1] = keyMember;
        }
        System.out.println("\n-list of members sorted by membership " +
                "expiration date-");
        for(int i = 0; i < size; i++)
            System.out.println(mlist[i].toString());
        System.out.println("-end of list-\n");
    }

    /**
     * Displays the list of members in database ordered last and first name.
     * Sorts the list by last and then first name using in-place insertion
     * sort. Iterates through the list of members and prints each member.
     */
    public void printByName() {
        for(int i = 1; i < size; ++i){
            Member keyMember = mlist[i];
            int j = i - 1;
            // Move each member alphabetically ahead of the key member up
            while(j >= 0 && mlist[j].compareTo(keyMember) > 0)
                mlist[j+1] = mlist[j--];
            mlist[j + 1] = keyMember;
        }
        System.out.println("\n-list of members sorted by last name, and " +
                "first name-");
        for(int i = 0; i < size; i++)
            System.out.println(mlist[i].toString());
        System.out.println("-end of list-\n");
    }

    /**
     * Displays the list of members in database ordered by membership fees.
     * Sorts the list by membership fees using in-place insertion sort.
     * Iterates through the list of members and prints each member.
     */
    public void printByMembershipFee(){
        for(int i = 1; i < size; ++i){
            Member keyMember = mlist[i];
            int j = i - 1;
            // Move each member alphabetically ahead of the key member up
            while(j >= 0 && mlist[j].compareTo(keyMember) > 0)
                mlist[j+1] = mlist[j--];
            mlist[j + 1] = keyMember;
        }
        System.out.println("\n-list of members with membership fees-");
        for(int i = 0; i < size; i++){
            String membershipInfo;
            if(mlist[i] instanceof Premium){
                membershipInfo = "Membership fee: $" +
                        ((Premium) mlist[i]).membershipFee();
            }
            else if(mlist[i] instanceof Family){
                membershipInfo = "Membership fee: $" +
                        ((Family) mlist[i]).membershipFee();
            }
            else{
                membershipInfo = "Membership fee: $" +
                        mlist[i].membershipFee();
            }
            System.out.println(mlist[i].toString() + ", " + membershipInfo);
        }
        System.out.println("-end of list-\n");
    }

    /**
     * Finds a member in database given a member.
     * Iterates through list of members and searches for corresponding member.
     * @param member the member that is being searched for.
     * @return the matching member in the database if found, null otherwise.
     */
    public Member findMember(Member member) {
        for (int i = 0; i < size; i++) {
            if (mlist[i].equals(member))
                return mlist[i];
        }
        return null;
    }

    /**
     * Gets the size of the list of members.
     * @return the size as an integer.
     */
    public int getSize() {
        return size;
    }

    /**
     * Creates a String containing all the members in the database.
     * @return a String with all the member information for each member.
     */
    @Override
    public String toString(){
        String members = "";
        for(int i = 0; i < size; i++)
            members += mlist[i].toString() + "\n";
        return members;
    }
}