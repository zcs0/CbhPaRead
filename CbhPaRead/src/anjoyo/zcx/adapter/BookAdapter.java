package anjoyo.zcx.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjoyo.aimo.R;
import com.anjoyo.manager.BooksFileDB;
import com.anjoyo.manager.DataManagerFactory;

import entity.BookFile;

public class BookAdapter extends BaseAdapter {
	Context mContext;
	List<BookFile> bookList;
	private boolean deleteStatus;
	public void setData(List<BookFile> bookList){
		this.bookList = bookList;
		notifyDataSetChanged();
	}
	public BookAdapter(Context con) {
		this.mContext = con;

	}
	public int getCount() {
		// TODO Auto-generated method stub
		return bookList==null?0:bookList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bookList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MyHolder myHolder = null;
		if(convertView==null){
			myHolder=new MyHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.book_list_item, null);
			myHolder.deleteImg = (ImageView)convertView.findViewById(R.id.btn_delete_book);
			myHolder.imageView = (ImageView) convertView
					.findViewById(R.id.item_image);
			myHolder.textView=(TextView)convertView.findViewById(R.id.item_textView);
			convertView.setTag(myHolder);
		}
		BookFile bookFile = bookList.get(position);
		myHolder=(MyHolder)convertView.getTag();
		myHolder.imageView.setBackgroundResource(R.drawable.orange_shelf_cover_shadow_online);
		
		float persent =0;
		if(bookFile.f_pager_count>0){
			persent =(1+bookFile.f_read_pager)*100f/bookFile.f_pager_count;
		}
		String p = String.format(bookFile.f_name+"\n%1$.2f", persent);
		myHolder.textView.setText(p+"%");
		if(deleteStatus){
			myHolder.deleteImg.setVisibility(View.VISIBLE);
			myHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BookFile book = bookList.get(position);
					deleteBook(book._id);
					bookList.remove(book);
				}
			});
		}else{
			myHolder.deleteImg.setVisibility(View.GONE);
		}
		return convertView;
	}
	private final class MyHolder {
		public ImageView deleteImg;
		ImageView imageView;
		TextView textView;
	}
	/**
	 * 删除状态
	 * @param selected
	 */
	public void setSelectItemDelete(boolean selected) {
		this.deleteStatus = selected;
		notifyDataSetInvalidated();
	}
	
	public boolean getSelectItemDelete(){
		return deleteStatus;
	}
	
	public void deleteBook(String bookId){
		BooksFileDB dbManager = DataManagerFactory.getDBManager(mContext);
		dbManager.delete(bookId);
		notifyDataSetChanged();
		Toast.makeText(mContext, "已删除", Toast.LENGTH_SHORT).show();
	}

}
