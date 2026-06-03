package LibrarySystem;// Stack for borrowing history. Most recent borrowed/returned activity is shown first.
// Original base used a linked stack. This version keeps that logic and stores LibrarySystem.HistoryRecord.

public class BorrowStack {
    private static class StackNode {
        private HistoryRecord record;
        private StackNode next;

        private StackNode(HistoryRecord record) {
            this.record = record;
        }
    }

    private StackNode top;

    // Original-style push for borrowed books. Kept for compatibility with the base idea.
    public void push(Book book, String matricNumber, String dateTime) {
        push(new HistoryRecord(matricNumber, book.getIsbn(), book.getTitle(), book.getAuthor(), "BORROW", dateTime));
    }

    // Added push for full activity record, including RETURN records.
    public void push(HistoryRecord record) {
        StackNode newNode = new StackNode(record);
        newNode.next = top;
        top = newNode;
    }

    public HistoryRecord peek() {
        if (top == null) {
            return null;
        }
        return top.record;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public String displayHistory() {
        if (isEmpty()) {
            return "No borrowing history found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-18s %-15s %-12s %-30s %-20s %-20s%n",
                "Date/Time", "Matric", "Action", "Title", "Author", "ISBN"));
        sb.append("-------------------------------------------------------------------------------------------------------------\n");

        StackNode current = top;
        while (current != null) {
            HistoryRecord r = current.record;
            sb.append(String.format("%-18s %-15s %-12s %-30s %-20s %-20d%n",
                    r.getDateTime(), r.getMatricNumber(), r.getAction(), r.getTitle(), r.getAuthor(), r.getIsbn()));
            current = current.next;
        }

        return sb.toString();
    }
}
