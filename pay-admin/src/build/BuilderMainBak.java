import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import com.pay.admin.build.build.Merger;

public class BuilderMainBak {
    public static void do1() throws Throwable {
        String hostName="app.y9688.com";
        String packageObj="eyJwcm9kdWN0SWQiOjIsImNoYW5uZWxJZCI6MSwibmFtZSI6Iuazoeazoem+mSjlnJ/osarniYgpIiwicGFja2FnZU5hbWUiOiJjb20udGh1bWJnYW1lcy5idWJibGVldmUiLCJ2ZXJzaW9uTmFtZSI6IjEuMC4xIiwidmVyc2lvbkNvZGUiOjEsImFwcE5hbWUiOiLms6Hms6Hpvpko5Zyf6LGq54mIKSIsInBheVNka3MiOiJzbXMzNjAiLCJwYXlTZGsiOiJzbXMzNjAiLCJsb2dpbk5hbWUiOiJhZHNmYXAxOTIyIiwibG9naW5QYXNzd29yZCI6InAxOXh4eHh4MjIiLCJzaG93QWN0aXZhdGVDb3VudCI6dHJ1ZSwic2hvd1BheUNvdW50IjpmYWxzZSwic2hvd1BheUVhcm5pbmciOmZhbHNlLCJwcm9tb3Rpb25UeXBlIjoiQ1BBIiwiYnVpbGRTdGF0dXMiOjQsInBheUhpbnRUeXBlIjowLCJpbmZvIjoiIiwiY3JlYXRlVGltZSI6Ikp1biAxMSwgMjAxNSAyOjQxOjI0IFBNIiwiYXV0b1N5bmMiOmZhbHNlLCJkaXNjb3VudFN0YXJ0Q291bnQiOjAsImRpc2NvdW50UmF0ZSI6MCwibWFya2V0UHJpY2UiOjAsImlkIjoyOTB9";
        String channelObj="eyJsb2dpbk5hbWUiOiJnYW5nZGl4eHh4eHh4IiwiZGlzcGxheU5hbWUiOiIxLua1i+ivleS4k+eUqCIsInBhc3N3b3JkIjoiZ2FuZ2RpODg4eHh4eHgiLCJjcmVhdGVUaW1lIjoiTWF5IDEzLCAyMDE1IDQ6NDA6NDMgUE0iLCJzaG93QWN0aXZhdGVDb3VudCI6dHJ1ZSwic2hvd1BheUNvdW50IjpmYWxzZSwic2hvd1BheUVhcm5pbmciOnRydWUsImhvdCI6NSwiYXV0b1N5bmMiOmZhbHNlLCJkaXNjb3VudFN0YXJ0Q291bnQiOjAsImRpc2NvdW50UmF0ZSI6MCwiaWQiOjF9";
        String productObj="eyJuYW1lIjoi5rOh5rOh6b6ZIiwicHJvamVjdE5hbWUiOiJCdWJibGVFdmVyeWRheSIsInBhY2thZ2VOYW1lIjoiY29tLnRodW1iZ2FtZXMuYnViYmxlZXZlIiwiZGVzY3JpcHRpb24iOiIiLCJjb2RlUGF0aCI6Ii93b3Jrc3BhY2UvcGF5L2NvZGUvcHJvamVjdHMvQnViYmxlRXZlcnlkYXkvVjEiLCJob3QiOjEwLCJjcmVhdGVUaW1lIjoiSnVuIDExLCAyMDE1IDI6MjM6MjYgUE0iLCJpZCI6Mn0=";
        doMerger(hostName,packageObj,channelObj,productObj);

    }
    private static void doMerger(String hostName, String pkgObj, String channelObj, String productObj) throws Throwable {
        JSONObject pkg = new JSONObject(base64Decode(pkgObj));
        JSONObject channel = new JSONObject(base64Decode(channelObj));
        JSONObject product = new JSONObject(base64Decode(productObj));
        String[] sdks = pkg.optString("paySdks").split(",");
        Merger.doMerge(hostName, pkg, channel, product, sdks);
    }
    private static String base64Decode(String src) {
        String result = "";
        try {
            result = new String(Base64.decodeBase64(src), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }
    public static void main(String[] args) throws Throwable {
        do1();
    }
}
