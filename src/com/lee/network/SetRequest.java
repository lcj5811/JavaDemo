package com.lee.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * @ClassName com.lee.network.SetRequest
 * @description
 * @author 凌霄
 * @data 2017年6月8日 下午4:53:13
 */
public class SetRequest {
	public static void main(String[] args) throws IOException {
		String urlStr = "http://www.qlcoder.com/task/17/solve";
		URL url = new URL(urlStr);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Referer", "http://cpc.people.com.cn");
		conn.setRequestProperty("Cookie",
				"gr_user_id=83c55620-6739-4e39-b137-9b19ddabaf31; uuid=593904a265117; %E8BF%99%E9%A2%98%E7%9A%84%E7%AD%94%E6%A1%88%E6%98%AForeo=eyJpdiI6IjZhQnVnTjM4dWRydjVLbTJXVTY5SUE9PSIsInZhbHVlIjoiMGZUQkx3eFdyVUxYaHVtVnJPajlCdz09IiwibWFjIjoiNDUzM2ZmNTUxNjI2ZGJjMmUxZDY3YTg5MDhlMTgwZTY0Yzc3MmE0MmRlYzQ2YjEwZWY1M2VjYTdjY2NhYjlmNCJ9; Hm_lvt_420590b976ac0a82d0b82a80985a3b8a=1496908917; Hm_lpvt_420590b976ac0a82d0b82a80985a3b8a=1496910212; gr_session_id_80dfd8ec4495dedb=c208a1ee-9285-479b-82a8-e549e3ac9b34; XSRF-TOKEN=eyJpdiI6IllnZ0NHZUp1MFN6NWZQT0l4cEdxZmc9PSIsInZhbHVlIjoiWWZST1lBVnhEM0ZDMXF2N21aUjFlRVBmYis3XC91NXltamZSMmh2SGJiK3lmaWVwUm5uNkdQS3ltWjV5a1luT25JZk03OGJCT3dsZkZEQXBuYkRvTXV3PT0iLCJtYWMiOiJmYzUxOTNiYzgzYWJmMTExYjEwZWJhMzZmMmVjODRmZjNkZDg5ZDEzZTRiZDdlMWU2ZDBmNWVmZDRhZTgwYjBlIn0%3D; laravel_session=eyJpdiI6IlRFN3RHTk40ZnNwMXlUa1wvOTZjWlZnPT0iLCJ2YWx1ZSI6Im81ZVhUcU9FbGc2T3h2MXFmXC9Ea2Y3QmhybDVIb2ZBWnZvWkpFQmJRWFFsbGRyaitkckdSRWprdEMzTys4M3pocjJYb3ZwdzgyZkZFOGx3RjgyQlR0Zz09IiwibWFjIjoiNDE1MWQyMzZmN2Y3ODA0MTdmZmFkN2Q4M2QwNWRkNWZhODY3ZmViYjZiOWFjODFkZjhhYjZjY2Y5YzcyMzEzZSJ9");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		PrintWriter pw = new PrintWriter(conn.getOutputStream());
		pw.print("answer=referer&_token=AFoeLsl2jsXFRmQ7nqWgggGFQGZYP2H5KLQx5mZv");
		pw.flush();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = null;
		if ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		reader.close();
	}
}
