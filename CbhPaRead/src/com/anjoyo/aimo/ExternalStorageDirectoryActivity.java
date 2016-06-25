﻿package com.anjoyo.aimo;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import anjoyo.ReadRoomActivity;
import anjoyo.zhou.thread.BookThread;

import com.anjoyo.manager.BooksFileDB;
import com.anjoyo.manager.DataManagerFactory;
import com.anjoyo.util.ListSortUtil;

import entity.BookFile;

public class ExternalStorageDirectoryActivity extends Activity {
	/**
	 * 本地文件类 显示 可读的文件列表 短按列表 可调用系统方法 打开 长按列表 可弹出dialog 可以选择 删除或修改
	 * 
	 */
	private String mSDCard = "/";
	private TextView mPath;
	EditText SouSuoEditText;
	ListView listView;
	int wenjianGeShu = 0;
	private PopupWindow menuPop, sousuoPop;
	View menuPopView, sousuoPopView;
	String sousuoWenJian[] = { "手动输入搜索", "TXT文件", "PNG文件", "PDF文件", "BMP文件",
			"GIF文件", "JPG文件" };
	Builder wenjianSelecte;
	public static String import_bookName;// 要导入书的名字
	BookThread bookThread = new BookThread();
	File sdpath = Environment.getExternalStorageDirectory();
	Thread threa;
	String fileEnds;// 文件后缀名
	private final String ROOT_PATH = Environment.getExternalStorageDirectory()
			.getPath();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wenianliulan);
		listView = (ListView) findViewById(R.id.list);
		mPath = (TextView) findViewById(R.id.text);
		mContext = ExternalStorageDirectoryActivity.this;
		createDialog();
		bookAdapter = new FileAdapter(ExternalStorageDirectoryActivity.this);
		listView.setAdapter(bookAdapter);
		SharedPreferences sp = DataManagerFactory.getSP(this);
		mCurrentFilePath = sp.getString(DataManagerFactory.last_open_sd_path, mCurrentFilePath);
		threa = new Thread(runn);//遍历文件
		threa.start();
		onItmeClick(listView);// 文件点击列表监听
		listjiantingLong();//列表长按监听 弹出dialog；
		//new FileAdapter
		mPath.setText("路径：" + mCurrentFilePath);
	}
	// 列表监听
	private void onItmeClick(final ListView listView) {
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				File mFile = new File(mFilePathList.get(arg2).f_path);
				fileEnds = mFile
						.getName()
						.substring(mFile.getName().lastIndexOf(".") + 1,
								mFile.getName().length()).toLowerCase();// 取出文件后缀名并转成小写
				if (mFile.canRead()) {// 如果该文件是可读的，我们进去查看文件
					if (mFile.isDirectory()) {// 如果是文件夹，则直接进入该文件夹，查看文件目录
						SharedPreferences sp = DataManagerFactory.getSP(ExternalStorageDirectoryActivity.this);
						Editor edit = sp.edit();
						edit.putString(DataManagerFactory.last_open_sd_path, mCurrentFilePath).commit();
						initFileListInfo(mFilePathList.get(arg2).f_path);
						bookAdapter.notifyDataSetChanged();
						listView.setSelection(0);
						mPath.setText("路径：" + mCurrentFilePath);
					} else if (fileEnds.equals("txt")) {//如果是一个可查看文件
						showPopupWindow(arg1,arg2,mFilePathList.get(arg2));
					} else {// 如果不是文件夹
						openFile(mFile);
					}
				}
			}
		});
	}

	public String mCurrentFilePath = mSDCard;// 用来保存 当前目录路径信息

	// 遍历文件 即扫描
	private void initFileListInfo(String filePath) {
		mCurrentFilePath = filePath;
		mFilePathList = new ArrayList<BookFile>();
		File mFile = new File(filePath);
		File[] mFiles = mFile.listFiles();// 遍历出该文件夹路径下的所有文件/文件夹
		/* 将所有文件信息添加到集合中 */
		for (File mCurrentFile : mFiles) {
			if (mCurrentFile.canRead()) {// 判断是否可读 判断是否可读 判断是否可读
//				mFileName.add(mCurrentFile.getName());
				mFilePathList.add(new BookFile(mCurrentFile));
			}
		}
		ListSortUtil.sortBookList(mFilePathList);
		mFilePathList = ListSortUtil.sortBookList(mFilePathList, true);
		if (!mCurrentFilePath.equals(mSDCard)) {
			initAddBackUp(filePath, mSDCard);
			isSouSuo = 1;
		}else{
			isSouSuo = 0;
		}
		/* 适配数据 */

	}

	// 顶部返回
	private void initAddBackUp(String filePath, String phone_sdcard) {

		if (!filePath.equals(phone_sdcard)) {
			/* 列表项的第1项设置为返回上一级 */
//			mFileName.add("BacktoUp");
			BookFile fileItem = new BookFile();
			fileItem.f_name = "BacktoUp";
			fileItem.f_path = new File(filePath).getParent();
			mFilePathList.add(0, fileItem);
//			mFilePathList.add(new FileItem(new File(filePath)));// 回到当前目录的父目录即回到上级
		}

	}

	// 适配器
	private Bitmap mImage, mAudio, mRar;
	private Bitmap mVideo, mFolder, mApk, mOthers, mTxt;
	private Bitmap mWeb;
	private Bitmap backImage, sanjiaoImage;
	private Context mContext;
//	private List<String> mFileNameList;
	private List<BookFile> mFilePathList;

	class FileAdapter extends BaseAdapter {
		public FileAdapter(Context context) {
			mContext = context;
			mImage = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.image);
			mAudio = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.audio);
			mVideo = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.video);
			mApk = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.apk);
			mTxt = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.text);
			mOthers = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.othersother);
			mFolder = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.folder);
			mRar = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.zip_icon);
			mWeb = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.web_browser);
			backImage = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.lib_back);
			sanjiaoImage = BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.meta_item_more);
		}

		public int getCount() {
			return mFilePathList==null?0:mFilePathList.size();
		}

		public Object getItem(int position) {
			return mFilePathList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup viewgroup) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater mLI = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mLI.inflate(R.layout.wenjianlistview, null);
				viewHolder.mIV = (ImageView) convertView
						.findViewById(R.id.image_list_childs);
				viewHolder.mTV = (TextView) convertView
						.findViewById(R.id.text_list_childs);
				viewHolder.backImage = (ImageView) convertView
						.findViewById(R.id.backImage);
				viewHolder.wenjianSum = (TextView) convertView
						.findViewById(R.id.text2_list_childs);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final File mFile = mFilePathList.get(position).getFile();
			

			if (mFile==null) {
				viewHolder.wenjianSum.setVisibility(View.GONE);
				viewHolder.mIV.setVisibility(View.GONE);
				viewHolder.backImage.setImageBitmap(backImage);
				viewHolder.mTV.setText("上一级");

			}else {
				String fileName = mFile.getName();
				String wenjianLength = daxiaoZhuanHuan((int) mFile.length());// 文件大小
				viewHolder.mIV.setVisibility(View.VISIBLE);
				viewHolder.wenjianSum.setVisibility(View.VISIBLE);
				viewHolder.backImage.setImageBitmap(sanjiaoImage);
				viewHolder.mTV.setText(fileName);

				if (mFile.isDirectory()) {

					try {// 防止异常
						File[] fi = mFile.listFiles();
						// 看 本文件夹下 有几个 可读文件
						for (File mCurrentFile : fi) {
							if (mCurrentFile.canRead()) {// 判断是否可读 判断是否可读 判断是否可读
								wenjianGeShu++;
							}
						}

					} catch (Exception e) {
					}

					viewHolder.mIV.setImageBitmap(mFolder);
					viewHolder.wenjianSum.setText(wenjianGeShu + "个文件");
					wenjianGeShu = 0;
				} else {

					fileEnds = fileName.substring(
							fileName.lastIndexOf(".") + 1, fileName.length())
							.toLowerCase();// 取出文件后缀名并转成小写
					if (fileEnds.equals("m4a") || fileEnds.equals("mp3")
							|| fileEnds.equals("mid") || fileEnds.equals("xmf")
							|| fileEnds.equals("ogg") || fileEnds.equals("wav")) {
						viewHolder.mIV.setImageBitmap(mVideo);
						viewHolder.wenjianSum.setText(fileEnds + "文件" + "  "
								+ wenjianLength);
					} else if (fileEnds.equals("3gp") || fileEnds.equals("mp4")) {
						viewHolder.mIV.setImageBitmap(mAudio);
						viewHolder.wenjianSum.setText(fileEnds + "文件" + "  "
								+ wenjianLength);
					} else if (fileEnds.equals("jpg") || fileEnds.equals("gif")
							|| fileEnds.equals("png")
							|| fileEnds.equals("jpeg")
							|| fileEnds.equals("bmp")) {
						viewHolder.mIV.setImageBitmap(mImage);
						viewHolder.wenjianSum.setText(fileEnds + "文件" + "  "
								+ wenjianLength);
					} else if (fileEnds.equals("apk")) {
						viewHolder.mIV.setImageBitmap(mApk);
						viewHolder.wenjianSum.setText(fileEnds + "文件" + "  "
								+ wenjianLength);
					} else if (fileEnds.equals("txt")) {
						viewHolder.mIV.setImageBitmap(mTxt);
						viewHolder.wenjianSum.setText(fileEnds + "文件" + "  "
								+ wenjianLength);
					} else if (fileEnds.equals("zip") || fileEnds.equals("rar")) {
						viewHolder.mIV.setImageBitmap(mRar);
						viewHolder.wenjianSum.setText(fileEnds + "文件" + "  "
								+ wenjianLength);
					} else if (fileEnds.equals("html")
							|| fileEnds.equals("htm") || fileEnds.equals("mht")) {
						viewHolder.mIV.setImageBitmap(mWeb);
						viewHolder.wenjianSum.setText(fileEnds + "文件" + "  "
								+ wenjianLength);
					} else {
						viewHolder.mIV.setImageBitmap(mOthers);
						viewHolder.wenjianSum.setText("未知文件" + "  "
								+ wenjianLength);

					}
				}

			}
			return convertView;
		}

		class ViewHolder {
			ImageView mIV, backImage;
			TextView mTV, wenjianSum;
		}

	}

	

	String WenJiancaozuo[] = { "重命名", "删除" };

	// 列表长按监听 弹出dialog；
	public void listjiantingLong() {
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				wenjianSelecte = new Builder(ExternalStorageDirectoryActivity.this);
				wenjianSelecte.setTitle("请选择操作");
				wenjianSelecte.setItems(WenJiancaozuo,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:// 重命名
									initRenameDialog(new File(mFilePathList.get(arg2).f_path));
									break;
								case 1:// 删除
									initDeleteDialog(new File(mFilePathList.get(arg2).f_path));
									break;
								}
							}
						});
				wenjianSelecte.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				wenjianSelecte.show();
				return false;
			}
		});
	}

	EditText renameEditT;
	AlertDialog renameDialog;

	// 重命名 /**调用弹出重命名框的方法*/
	private void initRenameDialog(final File file) {
		LayoutInflater mLI = LayoutInflater.from(ExternalStorageDirectoryActivity.this);
		LinearLayout mLL = (LinearLayout) mLI.inflate(
				R.layout.bendiwenjiancaozuo, null);
		renameEditT = (EditText) mLL.findViewById(R.id.renameEditT);
		renameEditT.setText(file.getName());

		renameDialog = new AlertDialog.Builder(ExternalStorageDirectoryActivity.this)
				.create();
		renameDialog.setView(mLL);
		renameDialog.setTitle("请不要修改文件格式名");
		renameDialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final String modifyFilePath = file.getParentFile().getPath()
						+ java.io.File.separator;
				final String newFilePath = modifyFilePath
						+ renameEditT.getText().toString();
				// 判断该新的文件名是否已经在当前目录下存在
				if (new File(newFilePath).exists()) {
					if (!renameEditT.getText().toString()
							.equals(file.getName())) {// 把“重命名”操作时没做任何修改的情况过滤掉
						// 弹出该新命名后的文件已经存在的提示，并提示接下来的操作
						new AlertDialog.Builder(ExternalStorageDirectoryActivity.this)
								.setTitle("提示!")
								.setMessage("该文件名已存在，是否要覆盖?")
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												file.renameTo(new File(
														newFilePath));
												Toast.makeText(
														ExternalStorageDirectoryActivity.this,
														"the file path is "
																+ new File(
																		newFilePath),
														Toast.LENGTH_SHORT)
														.show();
												initFileListInfo(file
														.getParentFile()
														.getPath());
											}
										}).setNegativeButton("取消", null).show();
					}
				} else {// 文件名不重复时直接修改文件名后再次刷新列表
					file.renameTo(new File(newFilePath));
					initFileListInfo(file.getParentFile().getPath());
					bookAdapter.notifyDataSetChanged();
					listView.setSelection(0);
					mPath.setText("路径：" + mCurrentFilePath);
				}
			}
		});
		renameDialog.setButton2("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		renameDialog.show();
	}

	/** 弹出删除文件/文件夹的弹出框 */
	private void initDeleteDialog(final File file) {
		new AlertDialog.Builder(ExternalStorageDirectoryActivity.this)
				.setTitle("提示!")
				.setMessage(
						"您确定要删除该" + (file.isDirectory() ? "文件夹" : "文件") + "吗?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (file.isFile()) {
							file.delete();// 是文件则直接删除
						} else {
							deleteFolder(file);// 是文件夹则用这个方法删除
						}
						initFileListInfo(file.getParent());// 重新遍历该文件的父目录
						bookAdapter.notifyDataSetChanged();
						listView.setSelection(0);
						mPath.setText("路径：" + mCurrentFilePath);
					}
				}).setNegativeButton("取消", null).show();
	}

	/** 删除文件夹的方法（删除该文件夹下的所有文件） */
	public void deleteFolder(File folder) {
		File[] fileArray = folder.listFiles();
		if (fileArray.length == 0) {// 空文件夹则直接删除
			folder.delete();
		} else {
			for (File currentFile : fileArray) {// 遍历该目录
				if (currentFile.exists() && currentFile.isFile()) {// 文件则直接删除
					currentFile.delete();
				} else {
					deleteFolder(currentFile);// 回调
				}
			}
			folder.delete();
		}
	}

	/** 调用系统的方法，来打开文件的方法 */
	private void openFile(File file) {
		if (file.isDirectory()) {
			initFileListInfo(file.getPath());
		} else {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), getMIMEType(file));
			startActivity(intent);
		}
	}

	/** 获得MIME类型的方法 */
	private String getMIMEType(File file) {
		String type = "";
		String fileName = file.getName();
		fileEnds = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length()).toLowerCase();// 取出文件后缀名并转成小写
		if (fileEnds.equals("m4a") || fileEnds.equals("mp3")
				|| fileEnds.equals("mid") || fileEnds.equals("xmf")
				|| fileEnds.equals("ogg") || fileEnds.equals("wav")) {
			type = "audio/*";// 系统将列出所有可能打开音频文件的程序选择器
		} else if (fileEnds.equals("3gp") || fileEnds.equals("mp4")) {
			type = "video/*";// 系统将列出所有可能打开视频文件的程序选择器
		} else if (fileEnds.equals("jpg") || fileEnds.equals("gif")
				|| fileEnds.equals("png") || fileEnds.equals("jpeg")
				|| fileEnds.equals("bmp")) {
			type = "image/*";// 系统将列出所有可能打开图片文件的程序选择器
		} else {
			type = "*/*"; // 系统将列出所有可能打开该文件的程序选择器
		}
		return type;
	}

	/**
	 * 返回按钮
	 * 
	 * @param v
	 */
	public void fanhui(View v) {
		finish();
	}

	// 点击菜单按钮
	public void BenDiWenJianMenuBtn(View v) {
		menuPopView = getLayoutInflater().inflate(R.layout.bendiwenjiancaidan,
				null);

		menuPop = new PopupWindow(menuPopView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		menuPop.setBackgroundDrawable(new BitmapDrawable());
		menuPop.setAnimationStyle(android.R.style.Animation_Translucent);// 系统动画

		menuPop.showAsDropDown(v, Gravity.RIGHT, -1);

		menuPop.setFocusable(true);
		menuPop.setOutsideTouchable(true);
		menuPop.update();

	}

	// 点击搜索按钮
	int isSouSuo = 0;// 用来判断是否进行过搜索 0 代表 没有 1代表有

	public void sousuo(final View v) {

		menuPop.dismiss();
		wenjianSelecte = new Builder(this);
		wenjianSelecte.setTitle("搜索");
		wenjianSelecte.setItems(sousuoWenJian,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which != 0) {
							mFilePathList.clear();// 清空list
						}

						switch (which) {
						case 0:// 手动输入搜索
							sousuoPopView = getLayoutInflater().inflate(
									R.layout.shoudongsousuo, null);
							SouSuoEditText = (EditText) sousuoPopView
									.findViewById(R.id.SouSuoEditText);

							sousuoPop = new PopupWindow(sousuoPopView,
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);
							sousuoPop
									.setBackgroundDrawable(new BitmapDrawable());
							sousuoPop
									.setAnimationStyle(android.R.style.Animation_Translucent);// 系统动画

							sousuoPop.showAsDropDown(v, 0, 0);

							sousuoPop.setFocusable(true);
							sousuoPop.setOutsideTouchable(true);
							sousuoPop.update();
							break;
						case 1:// TXT文件
							try {
								printAllFile(sdpath, ".txt");
							} catch (Exception e) {
							}
							break;
						case 2:// PNG文件
							try {
								printAllFile(sdpath, ".png");
							} catch (Exception e) {
							}
							break;
						case 3:// PDF文件
							try {
								printAllFile(sdpath, ".pdf");
							} catch (Exception e) {
							}
							break;
						case 4:// BMP文件
							try {
								printAllFile(sdpath, ".bmp");
							} catch (Exception e) {
							}
							break;
						case 5:// GIF文件
							try {
								printAllFile(sdpath, ".gif");
							} catch (Exception e) {
							}
							break;
						case 6:// JPG文件
							try {
								printAllFile(sdpath, ".jpg");
							} catch (Exception e) {
							}
							break;
						}
						// 绑定适配器
						if (which != 0) {
							bookAdapter.notifyDataSetChanged();
							listView.setSelection(0);
							mPath.setText("  搜索到" + mFilePathList.size() + "个文件");
						}

					}
				});
		wenjianSelecte.setPositiveButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		wenjianSelecte.show();
	}
	/**
	 * 加入本页所有文档
	 * @param v
	 */
	public void addAurrentDoc(View v){
		BooksFileDB dbManager = DataManagerFactory.getDBManager(this);
		List<BookFile> list = new ArrayList<BookFile>();
		for (BookFile book : mFilePathList) {
			if(book.f_path.length()>3&&(book.f_path.indexOf(".txt", book.f_path.length()-4)>=0||book.f_path.indexOf(".TXT", book.f_path.length()-4)>=0)){
				book.f_type = 2;
				list.add(book);
			}
		}
		long conut = dbManager.insterList(list);
		Toast.makeText(this, String.format("共更新%1$d", list.size()), Toast.LENGTH_SHORT).show();
	}

	// 手 动 输 入 搜 索 搜索按钮
	public void ShouSelect(View v) {
		if (!SouSuoEditText.getText().toString().equals("")) {
			mFilePathList.clear();// 清空list
			try {// 必须加 try catch
				printAllFile(sdpath, SouSuoEditText.getText().toString());
			} catch (Exception e) {
			}
			bookAdapter.notifyDataSetChanged();
			listView.setSelection(0);
			mPath.setText("  搜索到" + mFilePathList.size() + "个文件");
			sousuoPop.dismiss();
		} else {
			Toast.makeText(this, "请输入搜索条件", 1000).show();
		}

	}

	// 手 动 输 入 搜 索 取消按钮
	public void QuXiao(View v) {
		sousuoPop.dismiss();
	}

	/**
	 * 搜索 搜索用到 循环遍历
	 */
	public void printAllFile(File f, String str) {
		isSouSuo = 1;
		if (f.isFile()) {// 如果是文件
			if (f.toString().contains(str)) {
//				mFileName.add(f.getName());
				mFilePathList.add(new BookFile(f));
			}
		}
		if (f.isDirectory()) {// 如果是文件夹
			File[] f1 = f.listFiles();
			int len = f1.length;
			for (int i = 0; i < len; i++) {
				printAllFile(f1[i], str);
			}
		}

	}

	// 文件大小格式的转换
	private byte[] b = null;
	String sss = null;

	public String daxiaoZhuanHuan(int x) {
		if (x >= 1024 * 1024 * 1024) {// 转换成G
			b = (x * 0.9766 * 0.9766 * 0.9766 / 1000000000 + "").getBytes();
			sss = new String(b, 0, 6) + "G";
			return sss;
		} else if (x >= 1024 * 1024 && x < 1024 * 1024 * 1024) {// 转换成m
			b = (x * 0.9766 * 0.9766 / 1000000 + "").getBytes();
			sss = new String(b, 0, 6) + "m";
			return sss;
		} else if (x > 1024 && x <= 1024 * 1024) {// 转换成k
			b = (x * 0.9766 / 1000 + "").getBytes();
			sss = new String(b, 0, 6) + "k";
			return sss;
		} else {// 字节
			return x + "字节";
		}
	}

	// 当点击搜索 之后 若点击 back键 回到 搜索前的 文件列表
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isSouSuo == 1) {
				lanjieback(new File(mCurrentFilePath).getParent());
			} else {
				SharedPreferences sp = DataManagerFactory.getSP(ExternalStorageDirectoryActivity.this);
				Editor edit = sp.edit();
				edit.putString(DataManagerFactory.last_open_sd_path, "/").commit();
				finish();
			}
		}
		return true;
	}

	public void lanjieback(String path) {
		this.initFileListInfo(path);
		bookAdapter.notifyDataSetChanged();
		listView.setSelection(0);
		mPath.setText("路径：" + mCurrentFilePath);
	}

	// 等待 即熊猫转圈
	ProgressDialog proDialog;

	public void createDialog() {
		proDialog = new ProgressDialog(this);
		proDialog.show();
		proDialog.setContentView(R.layout.myprogressdialog);
	}
	
	// 处理耗时操作 即 扫描文件
	Runnable runn = new Runnable() {
		@Override
		public void run() {
			Message mag = ExternalStorageDirectoryActivity.this.handler.obtainMessage();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			initFileListInfo(mCurrentFilePath);
			mag.what = 2;
			mag.sendToTarget();
		}
	};
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				break;

			case 2:
				proDialog.dismiss();
				bookAdapter.notifyDataSetChanged();
				listView.setSelection(0);
				mPath.setText("路径：" + mCurrentFilePath);
				break;
			}
		};
	};
	private PopupWindow mPopuwindow;

	/*
	 * @弹出POPU MENU
	 */
	public void showPopupWindow(View v, int position, final BookFile mFile) {
		final int p = position;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.file_item_menu, null);
		mPopuwindow = new PopupWindow(layout,
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LinearLayout openFileBtn = (LinearLayout) layout
				.findViewById(R.id.openFileBtn);
		LinearLayout shelfFileBtn = (LinearLayout) layout
				.findViewById(R.id.shelfFileBtn);
		LinearLayout favoriteFileBtn = (LinearLayout) layout
				.findViewById(R.id.favoriteFileBtn);
		openFileBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {// 打开指定的文件
				mPopuwindow.dismiss();
				BooksFileDB dbManager = DataManagerFactory.getDBManager(ExternalStorageDirectoryActivity.this);
				mFile.f_type = 1;
				mFile.f_time= System.currentTimeMillis();
				String id = dbManager.insterOrUpdate(mFile);
				Intent intent = new Intent();
				intent.putExtra("file_id", id);
				intent.setClass(ExternalStorageDirectoryActivity.this, ReadRoomActivity.class);
				startActivity(intent);
				finish();
				
			}
		});
		shelfFileBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mFile.f_time= System.currentTimeMillis();
				mFile.f_type = 2;
				BooksFileDB dbManager = DataManagerFactory.getDBManager(ExternalStorageDirectoryActivity.this);
				String id = dbManager.insterOrUpdate(mFile);
				Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
				mPopuwindow.dismiss();
			}
		});
		favoriteFileBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mFile.f_time= System.currentTimeMillis();
				mFile.f_type =3;
				BooksFileDB dbManager = DataManagerFactory.getDBManager(ExternalStorageDirectoryActivity.this);
				String id = dbManager.insterOrUpdate(mFile);
				Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
				mPopuwindow.dismiss();
			}
		});
		ColorDrawable cd = new ColorDrawable(-0000);
		mPopuwindow.setBackgroundDrawable(cd);
		mPopuwindow.setBackgroundDrawable(cd);
		mPopuwindow.setOutsideTouchable(true);
		mPopuwindow.setFocusable(true);
		// showAsDropDown会把里面的view作为参照物，所以要那满屏幕parent
		// popupWindow.showAsDropDown(findViewById(R.id.tv_title), x, 10);
		if(position == mFilePathList.size()-1){
			mPopuwindow.showAtLocation(v, Gravity.BOTTOM, 0, v.getHeight());
		}else{
			mPopuwindow.showAsDropDown(v);
		}

	}
	File current_dir=null;
	private void browseTo(File dir) {

		if (dir.isDirectory()) {
//			pathTextView.setText(dir.getAbsolutePath());
			this.current_dir = dir;
			fill(current_dir.listFiles());
		}

	}
	private void fill(File[] files) {
		//
		if (mFilePathList == null) {
			mFilePathList = new ArrayList<BookFile>();
		}
		//
		mFilePathList.clear();
		Resources res = getResources();

		if (files != null) {
			for (File file : files) {
				mFilePathList.add(new BookFile(file));
			}
		}
		bookAdapter.notifyDataSetChanged();
		listView.setSelection(0);
	}
	
	
	//从小到1至-1大排
	private Comparator<BookFile> comparator = new Comparator<BookFile>() {

		@Override
		public int compare(BookFile lhs, BookFile rhs) {
			if(TextUtils.isEmpty(lhs.f_name)){
				return 1;
			}
			if(TextUtils.isEmpty(rhs.f_name)){
				return -1;
			}
			if(!TextUtils.isEmpty(lhs.f_name)&&TextUtils.isEmpty(rhs.f_name)){
				char one = lhs.f_name.charAt(0);
				char two = rhs.f_name.charAt(0);
				int r = one - two;
				return r == 0 ? 0 : r > 0 ? 1: -1;
			}
			return 0;
			
		}
	};
//======================================
//		
	private FileAdapter bookAdapter;

}
