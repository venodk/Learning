import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Complete the 'GetRejectedRequests' function below.
     *
     * The function is expected to return an INTEGER_ARRAY.
     * The function accepts following parameters:
     *  1. STRING_ARRAY requests
     *  2. INTEGER limit_per_second
     */

    public static List<Integer> GetRejectedRequests(List<String> requests, int limit_per_second) {
        // Write your code here
        List<Integer> result = new ArrayList<>();
        Map<String, Queue<Long>> map = new HashMap<>();

        for (String request : requests) {
            String[] split = request.split(" ");
            String reqID = split[0];
            String ip = split[1];
            long timeStamp = Long.parseLong(split[2]);
            if (!map.containsKey(ip)) {
                map.put(ip, new LinkedList<>());
            }
            Queue<Long> timeMap = map.get(ip);
            timeMap.add(timeStamp);
            if (timeMap.size() > limit_per_second) {
                while(!timeMap.isEmpty() && timeMap.peek() + 1000 <= timeStamp) {
                    timeMap.poll();
                }
                if (timeMap.size() > limit_per_second) {
                    result.add(Integer.parseInt(reqID));
                }
            }
        }
        return result;
    }
    public static class Solution {
        public static void main(String[] args) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

            int requestsCount = Integer.parseInt(bufferedReader.readLine().trim());

            List<String> requests = IntStream.range(0, requestsCount).mapToObj(i -> {
                        try {
                            return bufferedReader.readLine();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    })
                    .collect(toList());

            int limit_per_second = Integer.parseInt(bufferedReader.readLine().trim());

            List<Integer> result = Result.GetRejectedRequests(requests, limit_per_second);

            bufferedWriter.write(
                    result.stream()
                            .map(Object::toString)
                            .collect(joining("\n"))
                            + "\n"
            );

            bufferedReader.close();
            bufferedWriter.close();
        }
    }
}


