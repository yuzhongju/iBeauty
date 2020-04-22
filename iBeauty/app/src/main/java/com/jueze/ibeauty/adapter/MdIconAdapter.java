package com.jueze.ibeauty.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.jueze.colorpicker.ColorPickerDialog;
import com.jueze.colorpicker.ColorPickerPreference;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.MdIconBean;
import com.jueze.utils.FileUtil;
import com.jueze.utils.ImageUtil;
import com.jueze.utils.MyString;
import com.jueze.utils.ToastUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class MdIconAdapter extends RecyclerView.Adapter<MdIconAdapter.ViewHolder> implements Filterable {

    private List<MdIconBean> mIconDataList, filterList;
	private String query;
	private Context context;
	public MdIconAdapter(List<MdIconBean> list) {
        this.mIconDataList = list;
		this.filterList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
		View view;
        ImageView mIcon;
        TextView mName;
        public ViewHolder(View view) {
            super(view);
			context = view.getContext();
			this.view = view;
            mIcon = view.findViewById(R.id.md_icon_img);
            mName = view.findViewById(R.id.md_icon_name);
        }
    }

    @Override
    public MdIconAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_md_icons, parent, false));
    }

    @Override
    public void onBindViewHolder(MdIconAdapter.ViewHolder holder, int position) {
		final MdIconBean data=filterList.get(position);
        holder.mIcon.setImageBitmap(BitmapFactory.decodeFile(data.getIconPath()));
		if (TextUtils.isEmpty(query)) {
			holder.mName.setText(data.getIconName().replace("ic_", "").replace(".png", ""));
		} else {
			holder.mName.setText(MyString.highlightStr(Color.RED, data.getIconName().replace("ic_", "").replace(".png", ""), query));
		}

		holder.view.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					PopupMenu pop=new PopupMenu(context, v);
					pop.getMenuInflater().inflate(R.menu.menu_mdicon, pop.getMenu());
					pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

							@Override
							public boolean onMenuItemClick(MenuItem p1) {
								String rootDir=Environment.getExternalStorageDirectory().toString();
								String destDir="";

								switch (p1.getItemId()) {
									
									case R.id.export_iapp:
										destDir = rootDir + "/iApp/Userimg";
										if (FileUtil.isExists(destDir)) {
											save(data, destDir);
										} else {
											ToastUtil.show(context, "路径不存在");
										}
										break;
									case R.id.export_app:
										destDir = rootDir + "/iBeauty/icons";
										if (FileUtil.isExists(destDir)) {
											save(data, destDir);
										} else {
											ToastUtil.show(context, "路径不存在");
										}
										break;
								}
								return false;
							}
						});
					pop.show();
				}


			});
    }


	String[] aideProjects;
	boolean[] checkItems;
	private void showAideDialog(String destDir) {
		File[] fileList=new File(destDir).listFiles();

		aideProjects = new String[fileList.length];
		checkItems = new boolean[fileList.length];
		for (int i=0;i < fileList.length;i++) {
			aideProjects[i] = fileList[i].toString();
			checkItems[i] = false;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
			.setMultiChoiceItems(aideProjects, checkItems, new DialogInterface.OnMultiChoiceClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2, boolean p3) {
					checkItems[p2] = p3;
				}
			})
			.setPositiveButton("确认", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2) {
				}
			});

		dialog = builder.create();
		dialog.show();
	}


	AlertDialog dialog;

	EditText iconNameView;
	TextView iconPathNameView,iconColorView;
	CheckBox iconWhiteView, iconBlackView, iconGrayView;

	int color=0;

	int getColor() {
		return color;
	}
	void setColor(int color) {
		this.color = color;
	}
	String name="";
	String getName() {
		return name;
	}
	void setName(String name) {
		this.name = name;
	}
	String getDestDir() {
		return destDir;
	}
	private String destDir="";
	ColorPickerDialog colorPicker;
	MdIconBean iconData;

	private void save(MdIconBean data, final String destDir) {
		iconData = data;
		this.destDir = destDir;
		color = Color.BLACK;
		View dialogView = View.inflate(context, R.layout.dialog_export_icon, null);

		iconNameView = dialogView.findViewById(R.id.icon_name);
		iconPathNameView = dialogView.findViewById(R.id.icon_path_name);
		iconColorView = dialogView.findViewById(R.id.icon_color);
		iconWhiteView = dialogView.findViewById(R.id.icon_white);
		iconBlackView = dialogView.findViewById(R.id.icon_black);
		iconGrayView = dialogView.findViewById(R.id.icon_gray);

		setColor(Color.BLACK);
		updateIconInfo();

		iconNameView.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					try {
						iconPathNameView.setText(destDir + "/" + p1 + ".png");
					} catch (Exception w) {}
				}

				@Override
				public void afterTextChanged(Editable p1) {
				}
			});

		iconWhiteView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean checked) {
					if (checked) {
						iconBlackView.setChecked(false);
						iconGrayView.setChecked(false);
						setColor(Color.WHITE);
						updateIconInfo();
					}
				}
			});
		iconBlackView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean checked) {
					if (checked) {
						iconWhiteView.setChecked(false);
						iconGrayView.setChecked(false);
						setColor(Color.BLACK);
						updateIconInfo();
					}
				}
			});
		iconGrayView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean checked) {
					if (checked) {
						iconBlackView.setChecked(false);
						iconWhiteView.setChecked(false);
						setColor(Color.GRAY);
						updateIconInfo();
					}
				}
			});

		iconColorView.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					colorPicker = new ColorPickerDialog(context, getColor());
					colorPicker.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener(){

							@Override
							public void onColorChanged(int color) {
								setColor(color);
								updateIconInfo();
								if (iconWhiteView.isChecked()) iconWhiteView.setChecked(false);
								if (iconBlackView.isChecked()) iconBlackView.setChecked(false);
								if (iconGrayView.isChecked()) iconGrayView.setChecked(false);
							}
						});
					colorPicker.show();
				}
			});


		AlertDialog.Builder builder=new AlertDialog.Builder(context)
			.setTitle("导出图标")
			.setView(dialogView)
			.setPositiveButton("添加", null)
			.setNegativeButton("取消", null);
		dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener(){
				@Override
				public void onShow(DialogInterface p1) {
					dialog.getButton(AlertDialog.BUTTON_POSITIVE)
						.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View p1) {
								if (iconNameView.getText().length() > 0) {
									Bitmap bmp=BitmapFactory.decodeFile(iconData.getIconPath());
									bmp = ImageUtil.resetColor(bmp, "#" + ColorPickerPreference.convertToARGB(getColor()));
									ImageUtil.saveBitmap(bmp, iconPathNameView.getText().toString());
									ToastUtil.show(context, "已保存");
									dialog.dismiss();
								} else {
									iconNameView.setError("名称不能为空");
								}
							}
						});
				}
			});
		dialog.show();

	}

	private void updateIconInfo() {
		if (getColor() == Color.WHITE) {
			iconColorView.setTextColor(Color.BLACK);
		} else {
			iconColorView.setTextColor(Color.WHITE);
		}
		setName(iconData.replace(ColorPickerPreference.convertToARGB(getColor())).replace(".png", ""));
		iconNameView.setText(getName());
		iconNameView.setSelection(getName().length());
		iconColorView.setBackgroundColor(getColor());

		iconColorView.setText("#" + ColorPickerPreference.convertToARGB(getColor()));
		iconPathNameView.setText(destDir + "/" + getName() + ".png");
	}


    @Override
    public int getItemCount() {
        return filterList.size();
    }

	@Override
	public Filter getFilter() {
		return new Filter(){

			@Override
			protected Filter.FilterResults performFiltering(CharSequence p1) {
				query = p1.toString();
				if (TextUtils.isEmpty(query)) {
					filterList = mIconDataList;
				} else {
					List<MdIconBean> filters=new ArrayList<>();
					for (MdIconBean data : mIconDataList) {
						if (data.getIconName().contains(query)) {
							filters.add(data);
						}
					}
					filterList = filters;
				}
				FilterResults result=new FilterResults();
				result.values = filterList;
				return result;
			}

			@Override
			protected void publishResults(CharSequence p1, Filter.FilterResults p2) {
				filterList = (List<MdIconBean>) p2.values;
				notifyDataSetChanged();
			}
		};
	}


}
