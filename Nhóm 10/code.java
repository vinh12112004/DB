package project.com.edu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.Flow;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import gui.vn.com.JoptionPane;
import gui.vn.com.Jtabbedpane;
import gui.vn.com.MyFrame;

public class ProjectQuanLiSach {
 
	static JTextField txma=new JTextField(40);
	static JTextField txten=new JTextField(40);
	static JTextField txsdt=new JTextField(40);
	static JTextField txdiachi=new JTextField(40);
    static DefaultTableModel dftb=new DefaultTableModel();
    static JTable table=new JTable(dftb);
    static int currentRow=table.getSelectedRow();
	static Connection conn=null;
	static java.sql.Statement state=null;
	static PreparedStatement prestatement =null;
	static MyFrame mainframe=new MyFrame();
	public static void main(String[] args) {
		//create frame
		
		Container con=new Container();
		con.setLayout(new BorderLayout());
		
		//create thongtin and thuchien
		JPanel pnthongtinchucnang=new JPanel();
		pnthongtinchucnang.setLayout(new BoxLayout(pnthongtinchucnang, BoxLayout.X_AXIS));
		//create thongtin
		JPanel thongtin=new JPanel();
		thongtin.setLayout(new BoxLayout(thongtin, BoxLayout.Y_AXIS));
		//create border thong tin
		TitledBorder bdtitle=new TitledBorder(BorderFactory.createLineBorder(Color.RED),"Thông tin chi tiết");
        thongtin.setBorder(bdtitle);
		bdtitle.setTitleJustification(TitledBorder.LEFT);
		//create tittle
		JPanel pntitle=new JPanel();
		pntitle.setLayout(new FlowLayout());
		JLabel lbtitle=new JLabel("Thông tin nhà xuất bản");
		pntitle.add(lbtitle);
		thongtin.add(pntitle);
		//create manxb
		JPanel pnma=new JPanel();
		pnma.setLayout(new FlowLayout());
		JLabel lbma=new JLabel("Mã NXB");

		pnma.add(lbma);
		pnma.add(txma);
		thongtin.add(pnma);
		//create ten
		JPanel pnten=new JPanel();
		pnten.setLayout(new FlowLayout());
		JLabel lbten=new JLabel("Tên NXB");

		pnten.add(lbten);
		pnten.add(txten);
		thongtin.add(pnten);
		//create diachi
	    JPanel pndiachi=new JPanel();
		pndiachi.setLayout(new FlowLayout());
		JLabel lbdiachi=new JLabel("Địa chỉ");

		pndiachi.add(lbdiachi);
		pndiachi.add(txdiachi);
		thongtin.add(pndiachi);
		//create sdt
		JPanel pnsdt=new JPanel();
		pnsdt.setLayout(new FlowLayout());
		JLabel lbsdt=new JLabel("Điện thoại");

		pnsdt.add(lbsdt);
		pnsdt.add(txsdt);
		thongtin.add(pnsdt);
		
		
	    
		lbma.setPreferredSize(lbsdt.getPreferredSize());
		lbten.setPreferredSize(lbsdt.getPreferredSize());
		lbdiachi.setPreferredSize(lbsdt.getPreferredSize());
		//create button vetruoc and vesau
	    JPanel pnbuttonvetrcvesau=new JPanel();
	    JPanel pnbuttonvetrc=new JPanel();
	    JButton buttonvetrc=new JButton("Về trước");
	    buttonvetrc.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentRow>=0) {
					currentRow+=-1;
					table.setRowSelectionInterval(currentRow,currentRow);
					table.scrollRectToVisible(table.getCellRect(currentRow, 0, true));
		            hienthithongtinchitiet();
				}
				else {
					JOptionPane.showMessageDialog(null, "Không thể lùi thêm nữa");
				}
		   
			}
				
		});
	    pnbuttonvetrc.add(buttonvetrc);
	    JPanel pnbuttonvesau=new JPanel();
	    JButton buttonvesau=new JButton("Về sau");
	    buttonvesau.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
                if(currentRow<=dftb.getRowCount()) {
                	currentRow+=1;
                	table.setRowSelectionInterval(currentRow,currentRow);
     	            table.scrollRectToVisible(table.getCellRect(currentRow, 0, true));
     	            hienthithongtinchitiet();
                }
                else
                {
                	JOptionPane.showMessageDialog(null,"Không thể tiến thêm nữa");
                }
	           
			}
		});
	    pnbuttonvesau.add(buttonvesau);
	    pnbuttonvetrcvesau.add(buttonvetrc);
	    pnbuttonvetrcvesau.add(pnbuttonvesau);
	    thongtin.add(pnbuttonvetrcvesau);
	    
	    //create pnthuchien
	    JPanel pnthuchien=new JPanel();
	    pnthuchien.setLayout(new BoxLayout(pnthuchien, BoxLayout.Y_AXIS));
	    TitledBorder bdtitle1=new TitledBorder(BorderFactory.createLineBorder(Color.BLUE),"Thực hiện");
        pnthuchien.setBorder(bdtitle1);
		bdtitle1.setTitleJustification(TitledBorder.LEFT);
		
	    JPanel pnluu=new JPanel();
	    JButton luu=new JButton("Lưu  ");
	    pnluu.add(luu);
	    JPanel pnthem=new JPanel();
	    JButton them=new JButton("Thêm");
        them.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			  JOptionPane.showMessageDialog(null,"Điền thông tin lên bảng");
		      luu.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						prestatement = conn.prepareStatement("insert into publisher values (?,?,?,?)");
						prestatement.setString(1,txma.getText());
						prestatement.setString(2,txten.getText());
						prestatement.setString(3,txdiachi.getText());
						prestatement.setString(4, txsdt.getText());
						int x=prestatement.executeUpdate();
						if(x>0) {
							hienthidanhsach();
							JOptionPane.showMessageDialog(null,"Thêm thành công");
						}
						} catch (Exception e2) {}
					
				}
			});
				
			}
		});
	    pnthem.add(them);
	    JPanel pnsua=new JPanel();
	    JButton sua=new JButton("Sửa  ");
	    sua.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Điền thông tin cần sửa");
				luu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							prestatement=conn.prepareStatement("update publisher set id=?,name=?,address=?,phone=? where id=?");
							prestatement.setString(1,txma.getText());
							prestatement.setString(2,txten.getText());
							prestatement.setString(3,txdiachi.getText());
							prestatement.setString(4,txsdt.getText());
							prestatement.setString(5,(String) table.getValueAt(table.getSelectedRow(),0));
							int x=prestatement.executeUpdate();
							if(x>0) {
								hienthidanhsach();
								JOptionPane.showMessageDialog(null,"Sửa thành công");
							}
						} catch (Exception e2) {}
						
					}
				});
				
			}
		});
	    pnsua.add(sua);
	    JPanel pnxoa=new JPanel();
	    JButton xoa=new JButton("Xóa  ");
	    xoa.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					prestatement=conn.prepareStatement("delete from publisher where id=?");
					prestatement.setString(1,(String) table.getValueAt(table.getSelectedRow(),0));
					int x=prestatement.executeUpdate();
					if(x>0) {
						hienthidanhsach();
						JOptionPane.showMessageDialog(null,"Xoá thành công");
					}
				} catch (Exception e2) {}
				
			}
		});
	    pnxoa.add(xoa);
	    pnthuchien.add(pnthem);
	    pnthuchien.add(pnluu);
	    pnthuchien.add(pnsua);
	    pnthuchien.add(pnxoa);
	    
	    
	    
	    //create table;
	    JPanel pndsnhaxb=new JPanel();
	    pndsnhaxb.setLayout(new BorderLayout());
	    TitledBorder bdtitle2=new TitledBorder(BorderFactory.createLineBorder(Color.BLUE),"Danh sách nhà xuất bản");
        pndsnhaxb.setBorder(bdtitle2);
		bdtitle2.setTitleJustification(TitledBorder.LEFT);

	    dftb.addColumn("Mã nhà XB");
	    dftb.addColumn("Tên nhà XB");
	    dftb.addColumn("Địa chỉ");
	    dftb.addColumn("Điện thoại");

	    JScrollPane sc=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    pndsnhaxb.add(sc);
	    //create tim kiem button
	    JPanel pntimkiem=new JPanel();
	    pntimkiem.setLayout(new FlowLayout());
	    JButton timkiem=new JButton("Tìm kiếm");
	    timkiem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				hienthidanhsachtimkiem();
				
			}
		});
	    JComboBox sortbox=new JComboBox();
		sortbox.addItem("Sắp xếp từ A-Z");
		sortbox.addItem("Sắp xếp từ Z-A");
		JComboBox kindsortbox=new JComboBox();
		kindsortbox.addItem("Sắp xếp theo mã nhà XB");
		kindsortbox.addItem("Sắp xếp theo tên nhà XB");
		kindsortbox.addItem("Sắp xếp theo địa chỉ XB");
		kindsortbox.addItem("Sắp xếp theo số điện thoại XB");
		sortbox.setPreferredSize(kindsortbox.getPreferredSize());
		//create duyet button
		JButton btduyet=new JButton("Duyệt");
		btduyet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sortbox.getSelectedItem()=="Sắp xếp từ A-Z") {
					if(kindsortbox.getSelectedItem()=="Sắp xếp theo mã nhà XB") {
						try {
							state=conn.createStatement();
							ResultSet rs=state.executeQuery("select*from publisher order by id ASC");
							dftb.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id"));
								vec.add(rs.getString("name"));
								vec.add(rs.getString("address"));
								vec.add(rs.getString("phone"));
								dftb.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
					else if(kindsortbox.getSelectedItem()=="Sắp xếp theo tên nhà XB") {
						try {
							state=conn.createStatement();
							ResultSet rs=state.executeQuery("select*from publisher order by name ASC");
							dftb.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id"));
								vec.add(rs.getString("name"));
								vec.add(rs.getString("address"));
								vec.add(rs.getString("phone"));
								dftb.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
					else if(kindsortbox.getSelectedItem()=="Sắp xếp theo địa chỉ XB") {
						try {
							state=conn.createStatement();
							ResultSet rs=state.executeQuery("select*from publisher order by address ASC");
							dftb.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id"));
								vec.add(rs.getString("name"));
								vec.add(rs.getString("address"));
								vec.add(rs.getString("phone"));
								dftb.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
					else if(kindsortbox.getSelectedItem()=="Sắp xếp theo số điện thoại XB") {
						try {
							state=conn.createStatement();
							ResultSet rs=state.executeQuery("select*from publisher order by phone ASC");
							dftb.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id"));
								vec.add(rs.getString("name"));
								vec.add(rs.getString("address"));
								vec.add(rs.getString("phone"));
								dftb.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
				}
				else {
					if(kindsortbox.getSelectedItem()=="Sắp xếp theo mã nhà XB") {
						try {
							state=conn.createStatement();
							ResultSet rs=state.executeQuery("select*from publisher order by id DESC");
							dftb.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id"));
								vec.add(rs.getString("name"));
								vec.add(rs.getString("address"));
								vec.add(rs.getString("phone"));
								dftb.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
					else if(kindsortbox.getSelectedItem()=="Sắp xếp theo tên nhà XB") {
						try {
							state=conn.createStatement();
							ResultSet rs=state.executeQuery("select*from publisher order by name DESC");
							dftb.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id"));
								vec.add(rs.getString("name"));
								vec.add(rs.getString("address"));
								vec.add(rs.getString("phone"));
								dftb.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
					else if(kindsortbox.getSelectedItem()=="Sắp xếp theo địa chỉ XB") {
						try {
							state=conn.createStatement();
							ResultSet rs=state.executeQuery("select*from publisher order by address DESC");
							dftb.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id"));
								vec.add(rs.getString("name"));
								vec.add(rs.getString("address"));
								vec.add(rs.getString("phone"));
								dftb.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
					else if(kindsortbox.getSelectedItem()=="Sắp xếp theo số điện thoại XB") {
						try {
							state=conn.createStatement();
							ResultSet rs=state.executeQuery("select*from publisher order by phone DESC");
							dftb.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id"));
								vec.add(rs.getString("name"));
								vec.add(rs.getString("address"));
								vec.add(rs.getString("phone"));
								dftb.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
					
				}
				
			}
		});
		
		//create lichsu button
	    JButton btls=new JButton("Lịch sử");
	    btls.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MyFrame frls=new MyFrame();
				Container conls=new Container();
				conls.setLayout(new BorderLayout());
				DefaultTableModel dftbls=new DefaultTableModel();
				dftbls.addColumn("Mã thực hiện");
				dftbls.addColumn("Mã nhà XB");
				dftbls.addColumn("Tên nhà XB");
				dftbls.addColumn("Địa chỉ nhà XB");
				dftbls.addColumn("Số điện thoại");
				dftbls.addColumn("Thời gian đã thêm");
				dftbls.addColumn("Thời gian cập nhật");
				dftbls.addColumn("Thực hiện");
				JTable tbls=new JTable(dftbls);
				JScrollPane scls=new JScrollPane(tbls,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				try {
					state=conn.createStatement();
					ResultSet rs=state.executeQuery("select*from publisher_history");
					dftbls.setRowCount(0);
					while(rs.next()) {
						Vector<Object>vec= new Vector<Object>();
						vec.add(rs.getInt("id"));
						vec.add(rs.getString("publisher_id"));
						vec.add(rs.getString("name"));
						vec.add(rs.getString("address"));
						vec.add(rs.getString("phone"));
						vec.add(rs.getString("created_at"));
						vec.add(rs.getString("updated_at"));
						vec.add(rs.getString("action"));
						dftbls.addRow(vec);

			}
				} catch (Exception e2) {}
				conls.add(scls);
				frls.add(conls);
				frls.setTitle("Lịch sử thực hiện");
				frls.setVisible(true);
				frls.setDefaultCloseOperation(1);
				
			}
		});
	    btduyet.setPreferredSize(btls.getPreferredSize());
	    
	    pntimkiem.add(timkiem,FlowLayout.LEFT);
	    pntimkiem.add(sortbox,FlowLayout.CENTER);
	    pntimkiem.add(btls,FlowLayout.RIGHT);
	    pntimkiem.add(btduyet,FlowLayout.RIGHT);
	    pntimkiem.add(kindsortbox,FlowLayout.RIGHT);
	    
	    
	    
	   
	    //add image
	    them.setIcon(new ImageIcon("D:\\TeraBoxDownload\\VIDEO\\C6\\ProjectIcon\\tải_xuống-removebg-preview.png"));
	    luu.setIcon(new ImageIcon("D:\\TeraBoxDownload\\VIDEO\\C6\\ProjectIcon\\save-512-removebg-preview.png"));
	    sua.setIcon(new ImageIcon("D:\\TeraBoxDownload\\VIDEO\\C6\\ProjectIcon\\pngtree-vector-wrench-icon-png-image_515386-removebg-preview.png"));
	    xoa.setIcon(new ImageIcon("D:\\TeraBoxDownload\\VIDEO\\C6\\ProjectIcon\\x-delete-button-png-15.png"));
	    buttonvetrc.setIcon(new ImageIcon("D:\\TeraBoxDownload\\VIDEO\\C6\\ProjectIcon\\png-transparent-computer-icons-arrow-previous-button-angle-rectangle-triangle-thumbnail-removebg-preview.png"));
	    buttonvesau.setIcon(new ImageIcon("D:\\TeraBoxDownload\\VIDEO\\C6\\ProjectIcon\\images-removebg-preview.png"));
	    timkiem.setIcon(new ImageIcon("D:\\TeraBoxDownload\\VIDEO\\C6\\ProjectIcon\\pngtree-vector-search-icon-png-image_320926-removebg-preview.png"));
		//set layout
		pnthongtinchucnang.add(thongtin);
		pnthongtinchucnang.add(pnthuchien);
		con.add(pnthongtinchucnang,BorderLayout.NORTH);
		con.add(pndsnhaxb,BorderLayout.CENTER);
		con.add(pntimkiem,BorderLayout.SOUTH);
		mainframe.add(con);
		mainframe.pack();
		mainframe.setTitle("Quản lý sách");
		mainframe.setVisible(false);
		connectDB();
	    hienthidanhsach();
	    table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {

				hienthithongtinchitiet();
				currentRow=table.getSelectedRow();
				
				
				
			}
		});
        //login window
	    loginUI();
	    
	}
    
	
	private static void loginUI() {
		hienthi();
		}
    
	public static void hienthi() {
		//create frame
		MyFrame frame=new MyFrame();
		Container con=new Container();
		con.setLayout(new BorderLayout());
		
		//create title dang nhap
		JPanel pntittle=new JPanel();
		JLabel lbtitle=new JLabel("ĐĂNG NHẬP HỆ THỐNG");
		pntittle.add(lbtitle);
		
		//create thongtindangnhap
		JPanel pnimagethongtin=new JPanel();
		pnimagethongtin.setLayout(new BoxLayout(pnimagethongtin,BoxLayout.X_AXIS));
		TitledBorder bdtitle3=new TitledBorder(BorderFactory.createLineBorder(Color.BLUE),null);
        pnimagethongtin.setBorder(bdtitle3);
		bdtitle3.setTitleJustification(TitledBorder.LEFT);
		//create image
		JPanel pnimage=new JPanel();
		JLabel lbimage=new JLabel();
		ImageIcon image=new ImageIcon("D:\\TeraBoxDownload\\VIDEO\\C6\\ProjectIcon\\sticker-png-user-login-session-password-business-others-miscellaneous-business-desktop-wallpaper-form-authentication-thumbnail-removebg-preview.png");
		lbimage.setIcon(image);
		pnimage.add(lbimage);
		pnimagethongtin.add(pnimage);
		TitledBorder bdtitle2=new TitledBorder(BorderFactory.createLineBorder(Color.BLUE),null);
        pnimage.setBorder(bdtitle2);
		bdtitle2.setTitleJustification(TitledBorder.LEFT);
		//create thongtin
		JPanel pnthongtin=new JPanel();
		pnthongtin.setLayout(new BoxLayout(pnthongtin, BoxLayout.Y_AXIS));
		JPanel pnuser=new JPanel();
		JLabel lbuser=new JLabel("Tài Khoản:");
		JTextField txuser=new JTextField(20);
		pnuser.add(lbuser);
		pnuser.add(txuser);
		JPanel pnpass=new JPanel();
		JLabel lbpass=new JLabel("Mật Khẩu:");
		JPasswordField txpass=new JPasswordField(20);
		lbpass.setPreferredSize(lbuser.getPreferredSize());
		pnpass.add(lbpass);
		pnpass.add(txpass);
		pnthongtin.add(pnuser);
		pnthongtin.add(pnpass);
		pnimagethongtin.add(pnthongtin);
		TitledBorder bdtitle1=new TitledBorder(BorderFactory.createLineBorder(Color.RED),"Thông tin đăng nhập");
        pnthongtin.setBorder(bdtitle1);
		bdtitle1.setTitleJustification(TitledBorder.LEFT);
		
		//create button
		JPanel pnbutton=new JPanel();
		pnbutton.setLayout(new FlowLayout());
		JButton btdangnhap=new JButton("Đăng nhập");
		btdangnhap.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				check(txuser.getText(),txpass.getText());
				
			}
		});
		JButton btdangky=new JButton("Đăng ký");
		btdangky.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MyFrame framedangky=new MyFrame();
				JPanel pnthongtin1=new JPanel();
				pnthongtin1.setLayout(new BoxLayout(pnthongtin1, BoxLayout.Y_AXIS));
				JPanel pnuser1=new JPanel();
				JLabel lbuser1=new JLabel("Tài Khoản:");
				JTextField txuser1=new JTextField(20);
				pnuser1.add(lbuser1);
				pnuser1.add(txuser1);
				JPanel pnpass1=new JPanel();
				JLabel lbpass1=new JLabel("Mật Khẩu:");
				JPasswordField txpass1=new JPasswordField(20);
				pnpass1.add(lbpass1);
				pnpass1.add(txpass1);
				JPanel pnpass2=new JPanel();
				JLabel lbpass2=new JLabel("Nhập Lại Mật Khẩu:");
				JPasswordField txpass2=new JPasswordField(20);
				pnpass2.add(lbpass2);
				pnpass2.add(txpass2);
				
				lbuser1.setPreferredSize(lbpass2.getPreferredSize());
				lbpass1.setPreferredSize(lbpass2.getPreferredSize());

				pnthongtin1.add(pnuser1);
				pnthongtin1.add(pnpass1);
				pnthongtin1.add(pnpass2);

				
				//create button luu
				JPanel pnluu=new JPanel();
				JButton luu=new JButton("Lưu");
				luu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(txpass1.getText().equals(txpass2.getText())) {
							try {
								prestatement=conn.prepareStatement("insert into account values (?,?)");
								prestatement.setString(1,txuser1.getText());
								prestatement.setString(2,txpass1.getText());
								int x=prestatement.executeUpdate();
								if(x>0) {
									JOptionPane.showMessageDialog(null,"Tạo tài khoản thành công");
								}
								else {
									JOptionPane.showMessageDialog(null,"Tạo tài khoản thất bại");
								}
							} catch (Exception e2) {
								// TODO: handle exception
							}
						}
						else if(txpass1.getText()!=txpass2.getText()) {
							JOptionPane.showMessageDialog(null,"Tạo tài khoản thất bại vui lòng kiểm tra mật khẩu  xác nhận");
						}
						
					}
				});
				pnluu.add(luu);
				
				framedangky.add(pnthongtin1,BorderLayout.CENTER);
				framedangky.add(pnluu,BorderLayout.SOUTH);
				framedangky.pack();
				framedangky.setVisible(true);
				framedangky.setDefaultCloseOperation(1);


				
			}
		});
		pnbutton.add(btdangnhap,FlowLayout.LEFT);
		pnbutton.add(btdangky,FlowLayout.CENTER);
		btdangky.setPreferredSize(btdangnhap.getPreferredSize());

		
		
		
		//set layout
		con.add(pntittle,BorderLayout.NORTH);
		con.add(pnimagethongtin,BorderLayout.CENTER);
		con.add(pnbutton,BorderLayout.SOUTH);
		frame.add(con);
		frame.pack();
		frame.setVisible(true);
		frame.setTitle("Login");
		frame.setDefaultCloseOperation(1);
		

	}

	public static void check(String user,String pass) {
	    	try {
				prestatement=conn.prepareStatement("select*from account where username=? and password=?");
				prestatement.setString(1,user);
				prestatement.setString(2,pass);
				ResultSet rs=prestatement.executeQuery();
				if(rs.next()) {
                     mainframe.setVisible(true);
                     mainframe.setDefaultCloseOperation(1);
				}
				else {
					JOptionPane.showMessageDialog(null,"Tài khoản không tồn tại hoặc sai tên tài khoản hoặc mật khẩu");
				}
			} catch (Exception e) {}
	    	
	    }
	
	private static void hienthithongtinchitiet() {
		txma.setText((String) table.getValueAt(table.getSelectedRow(), 0));
		txten.setText((String) table.getValueAt(table.getSelectedRow(), 1));
		txdiachi.setText((String) table.getValueAt(table.getSelectedRow(), 2));
		txsdt.setText((String) table.getValueAt(table.getSelectedRow(),3));

		
	}


	private static void hienthidanhsach() {

		try {
			state=conn.createStatement();
			ResultSet rs=state.executeQuery("select*from publisher");
			dftb.setRowCount(0);
			while(rs.next()) {
				Vector<Object>vec=new Vector<Object>();
				vec.add(rs.getString("id"));
				vec.add(rs.getString("name"));
				vec.add(rs.getString("address"));
				vec.add(rs.getString("phone"));
				dftb.addRow(vec);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}


	private static void connectDB() {
		try {
			Class.forName("org.postgresql.Driver");
			conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/book","postgres","272004");
			if(conn==null) {
            System.out.println("failed");
			}
			else System.out.println("success");
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}


	protected static void hienthidanhsachtimkiem() {
		//create frame
		MyFrame frame=new MyFrame();
		Container con=new Container();
		con.setLayout(new BorderLayout());
		DefaultTableModel dftbsach=new DefaultTableModel();
		//create timkiem theo id
		JPanel pnnhaptk=new JPanel();
		pnnhaptk.setLayout(new FlowLayout());
		JLabel lbnhaptk=new JLabel("Nhập mã nhà XB cần tìm:");
		JTextField txnhaptk=new JTextField(20);
		JButton btnhaptk=new JButton("Tìm kiếm");
		btnhaptk.addActionListener(new ActionListener() {

			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					prestatement=conn.prepareStatement("select*from book where id=?");
					prestatement.setString(1,txnhaptk.getText());
					ResultSet rs=prestatement.executeQuery();
					dftbsach.setRowCount(0);
					while(rs.next()) {
						Vector<Object>vec=new Vector<Object>();
						vec.add(rs.getString("id1"));
						vec.add(rs.getString("name1"));
						vec.add(rs.getString("id"));
						vec.add(rs.getInt("author_id"));						
						dftbsach.addRow(vec);
					}
				} catch (Exception e3) {}
				
			}
		});
		JButton btreset=new JButton("Reset");
		btreset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txnhaptk.setText(null);
				try {
					state=conn.createStatement();
					ResultSet rs=state.executeQuery("select*from book");
					dftbsach.setRowCount(0);
					while(rs.next()) {
						Vector<Object>vec=new Vector<Object>();
						vec.add(rs.getString("id1"));
						vec.add(rs.getString("name1"));
						vec.add(rs.getString("id"));
						vec.add(rs.getInt("author_id"));
						dftbsach.addRow(vec);
					}
				} catch (Exception e3) {}
				
			}
		});
		pnnhaptk.add(lbnhaptk,FlowLayout.LEFT);
		pnnhaptk.add(txnhaptk,FlowLayout.CENTER);
		pnnhaptk.add(btreset,FlowLayout.RIGHT);
		pnnhaptk.add(btnhaptk,FlowLayout.RIGHT);
		btreset.setPreferredSize(btnhaptk.getPreferredSize());
		
		
		
		//create table hienthicacsach
		JPanel pnsach= new JPanel();
		pnsach.setLayout(new BoxLayout(pnsach, BoxLayout.X_AXIS));
		dftbsach.addColumn("Mã sách");
		dftbsach.addColumn("Tên sách");
		dftbsach.addColumn("Mã nhà XB");
		dftbsach.addColumn("Mã tác giả");
		JTable tablesach=new JTable(dftbsach);
		JScrollPane sc=new JScrollPane(tablesach,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnsach.add(sc);
        try {
			state=conn.createStatement();
			ResultSet rs=state.executeQuery("select*from book");
			dftbsach.setRowCount(0);
			while(rs.next()) {
				Vector<Object>vec=new Vector<Object>();
				vec.add(rs.getString("id1"));
				vec.add(rs.getString("name1"));
				vec.add(rs.getString("id"));
				vec.add(rs.getInt("author_id"));
				dftbsach.addRow(vec);
			}
		} catch (Exception e) {}
        
        
		
        //create sort tool
        JPanel pnsort=new JPanel();
        pnsort.setLayout(new FlowLayout());
        JComboBox sortbox=new JComboBox();
		sortbox.addItem("Sắp xếp từ A-Z");
		sortbox.addItem("Sắp xếp từ Z-A");
		JComboBox kindsortbox=new JComboBox();
		kindsortbox.addItem("Sắp xếp theo mã sách");
		kindsortbox.addItem("Sắp xếp theo tên sách");
		kindsortbox.addItem("Sắp xếp theo mã nhà XB");
		sortbox.setPreferredSize(kindsortbox.getPreferredSize());
		JButton btduyet=new JButton("Duyệt");
		btduyet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sortbox.getSelectedItem()=="Sắp xếp từ A-Z") {
					if(kindsortbox.getSelectedItem()=="Sắp xếp theo mã sách") {
						try {
							state=conn.createStatement();
							prestatement=conn.prepareStatement("select*from book where id=? order by id1 ASC");
							ResultSet rs=null;
							if(txnhaptk.getText().isEmpty()) {
								rs=state.executeQuery("select*from book order by id1 ASC");
							}
							else  {
								prestatement.setString(1,txnhaptk.getText());
								rs=prestatement.executeQuery();
							}
							
							dftbsach.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id1"));
								vec.add(rs.getString("name1"));
								vec.add(rs.getString("id"));
								vec.add(rs.getInt("author_id"));
								dftbsach.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
					else if(kindsortbox.getSelectedItem()=="Sắp xếp theo tên sách") {
						try {
							state=conn.createStatement();
							prestatement=conn.prepareStatement("select*from book where id=? order by name1 ASC");
							ResultSet rs;
							if(txnhaptk.getText().isEmpty()) {
								rs=state.executeQuery("select*from book order by name1 ASC");
							}
							else {
								prestatement.setString(1,txnhaptk.getText());
								rs=prestatement.executeQuery();
							}
							
							
							dftbsach.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id1"));
								vec.add(rs.getString("name1"));
								vec.add(rs.getString("id"));
								vec.add(rs.getInt("author_id"));
								dftbsach.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
					else if(kindsortbox.getSelectedItem()=="Sắp xếp theo mã nhà XB") {
						try {
							state=conn.createStatement();
							prestatement=conn.prepareStatement("select*from book where id=? order by id ASC");
							ResultSet rs=null;
							if(txnhaptk.getText().isEmpty()) {
								 rs=state.executeQuery("select*from book order by id ASC");
							}
							else
							{
								prestatement.setString(1,txnhaptk.getText());
								rs=prestatement.executeQuery();
							}
							
							dftbsach.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id1"));
								vec.add(rs.getString("name1"));
								vec.add(rs.getString("id"));
								vec.add(rs.getInt("author_id"));
								dftbsach.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
				
					}
				
				else {
					if(kindsortbox.getSelectedItem()=="Sắp xếp theo mã sách") {
						try {
							state=conn.createStatement();
							prestatement=conn.prepareStatement("select*from book where id=? order by id1 DESC");
							ResultSet rs=null;
							if(txnhaptk.getText().isEmpty()) {
								rs=state.executeQuery("select*from book order by id1 DESC");
							}
							else  {
								prestatement.setString(1,txnhaptk.getText());
								rs=prestatement.executeQuery();
							}
							
							dftbsach.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id1"));
								vec.add(rs.getString("name1"));
								vec.add(rs.getString("id"));
								vec.add(rs.getInt("author_id"));
								dftbsach.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
					else if(kindsortbox.getSelectedItem()=="Sắp xếp theo tên sách") {
						try {
							state=conn.createStatement();
							prestatement=conn.prepareStatement("select*from book where id=? order by name1 DESC");
							ResultSet rs;
							if(txnhaptk.getText().isEmpty()) {
								rs=state.executeQuery("select*from book order by name1 DESC");
							}
							else {
								prestatement.setString(1,txnhaptk.getText());
								rs=prestatement.executeQuery();
							}
							
							
							dftbsach.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id1"));
								vec.add(rs.getString("name1"));
								vec.add(rs.getString("id"));
								vec.add(rs.getInt("author_id"));
								dftbsach.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}
					else if(kindsortbox.getSelectedItem()=="Sắp xếp theo mã nhà XB") {
						try {
							state=conn.createStatement();
							prestatement=conn.prepareStatement("select*from book where id=? order by id DESC");
							ResultSet rs=null;
							if(txnhaptk.getText().isEmpty()) {
								 rs=state.executeQuery("select*from book order by id DESC");
							}
							else
							{
								prestatement.setString(1,txnhaptk.getText());
								rs=prestatement.executeQuery();
							}
							
							dftbsach.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id1"));
								vec.add(rs.getString("name1"));
								vec.add(rs.getString("id"));
								vec.add(rs.getInt("author_id"));
								dftbsach.addRow(vec);
							}
							
						} catch (Exception e2) {}
					}	
				}
				
			}
		});
		JButton luu=new JButton("Lưu");
		luu.setPreferredSize(btduyet.getPreferredSize());
		JMenuItem chitietthongtin=new JMenuItem("Thông tin tác giả");
		JMenuItem sua=new JMenuItem("Sửa");
		sua.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					prestatement=conn.prepareStatement("update book set id1=?,name1=?,id=?,author_id=? where id1=?");
					prestatement.setString(1,(String) tablesach.getValueAt(tablesach.getSelectedRow(), 0));
					prestatement.setString(2,(String) tablesach.getValueAt(tablesach.getSelectedRow(), 1));
					prestatement.setString(3,(String) tablesach.getValueAt(tablesach.getSelectedRow(), 2));
					prestatement.setInt(4,(int) tablesach.getValueAt(tablesach.getSelectedRow(), 3));
					prestatement.setString(5,(String) tablesach.getValueAt(tablesach.getSelectedRow(), 0));
					int x=prestatement.executeUpdate();
					if(x>0) {
						try {
							state=conn.createStatement();
							ResultSet rs=state.executeQuery("select*from book");
							dftbsach.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id1"));
								vec.add(rs.getString("name1"));
								vec.add(rs.getString("id"));
								vec.add(rs.getInt("author_id"));
								dftbsach.addRow(vec);
							}
						} catch (Exception e2) {}
						JOptionPane.showMessageDialog(null,"Sửa thành công");
					}
				} catch (Exception e2) {}
				
			}
		});
		JMenuItem xoa=new JMenuItem("Xóa");
		xoa.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					prestatement=conn.prepareStatement("delete from book where id1=?");
					prestatement.setString(1,(String) tablesach.getValueAt(tablesach.getSelectedRow(),0));
					int x=prestatement.executeUpdate();
					if(x>0) {
						try {
							state=conn.createStatement();
							ResultSet rs=state.executeQuery("select*from book");
							dftbsach.setRowCount(0);
							while(rs.next()) {
								Vector<Object>vec=new Vector<Object>();
								vec.add(rs.getString("id1"));
								vec.add(rs.getString("name1"));
								vec.add(rs.getString("id"));
								vec.add(rs.getInt("author_id"));
								dftbsach.addRow(vec);
							}
						} catch (Exception e2) {}
						
						JOptionPane.showMessageDialog(null,"Xoá thành công");
					}
				} catch (Exception e2) {}
				
			}
		});
		JMenuItem them1=new JMenuItem("Thêm");
		them1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//front end
				MyFrame framenhapdulieu=new MyFrame();
				Container connhapdulieu=new Container();
				connhapdulieu.setLayout(new BoxLayout(connhapdulieu,BoxLayout.Y_AXIS));
				//ID TEXT
				JPanel pnidsach=new JPanel();
				pnidsach.setLayout(new FlowLayout());
				JLabel lbidsach=new JLabel("Nhập mã sách");
				JTextField txidsach= new JTextField(20);
				pnidsach.add(lbidsach);
				pnidsach.add(txidsach);
				//NAME TEXT
				JPanel pnnamesach=new JPanel();
				pnnamesach.setLayout(new FlowLayout());
				JLabel lbnamesach=new JLabel("Nhập tên sách");
				JTextField txnamesach= new JTextField(20);
				pnnamesach.add(lbnamesach);
				pnnamesach.add(txnamesach);
				//IDNXB TEXT
				JPanel pnidnxb=new JPanel();
				pnidnxb.setLayout(new FlowLayout());
				JLabel lbidnxb=new JLabel("Nhập mã NXB");
				JTextField txidnxb= new JTextField(20);
				pnidnxb.add(lbidnxb);
				pnidnxb.add(txidnxb);
				//IDAUTHOR TEXT
				JPanel pnidtg=new JPanel();
				pnidtg.setLayout(new FlowLayout());
				JLabel lbidtg=new JLabel("Nhập mã tác giả");
				JTextField txidtg= new JTextField(20);
				pnidtg.add(lbidtg);
				pnidtg.add(txidtg);
                
				JPanel pnluu=new JPanel();
				pnluu.setLayout(new FlowLayout());
			    JButton luu=new JButton("Lưu");
				luu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
					    try {
					    	prestatement=conn.prepareStatement("insert into book values (?,?,?,?)");
							prestatement.setString(1,txidsach.getText());
							prestatement.setString(2,txnamesach.getText());
							prestatement.setString(3,txidnxb.getText());
							prestatement.setInt(4,Integer.parseInt(txidtg.getText()));
							int x=prestatement.executeUpdate();
							if(x>0) {
								try {
									state=conn.createStatement();
									ResultSet rs=state.executeQuery("select*from book");
									dftbsach.setRowCount(0);
									while(rs.next()) {
										Vector<Object>vec=new Vector<Object>();
										vec.add(rs.getString("id1"));
										vec.add(rs.getString("name1"));
										vec.add(rs.getString("id"));
										vec.add(rs.getInt("author_id"));
										dftbsach.addRow(vec);
									}
								} catch (Exception e2) {}
								JOptionPane.showMessageDialog(null,"Thêm thành công");
							}
						} catch (SQLException e1) {

							System.out.println("lỗi chèn");
						}
					    
						
					}
				});
				pnluu.add(luu);
				
				//setlayout
				lbidsach.setPreferredSize(lbidtg.getPreferredSize());
				lbnamesach.setPreferredSize(lbidtg.getPreferredSize());
				lbidnxb.setPreferredSize(lbidtg.getPreferredSize());
				connhapdulieu.add(pnidsach);
				connhapdulieu.add(pnnamesach);
				connhapdulieu.add(pnidnxb);
				connhapdulieu.add(pnidtg);
				connhapdulieu.add(pnluu);
				framenhapdulieu.add(connhapdulieu);
				framenhapdulieu.pack();
				framenhapdulieu.setVisible(true);
				framenhapdulieu.setDefaultCloseOperation(1);
				
			}
		});
        JPopupMenu popmenu=new JPopupMenu();
        popmenu.add(chitietthongtin);
        popmenu.add(sua);
        popmenu.add(xoa);
        popmenu.add(them1);
		tablesach.setComponentPopupMenu(popmenu);

	    pnsort.add(sortbox,FlowLayout.LEFT);
	    pnsort.add(kindsortbox,FlowLayout.CENTER);
	    pnsort.add(luu,FlowLayout.RIGHT);
	    pnsort.add(btduyet,FlowLayout.RIGHT);
	    
	    
	    //create tac gia thong tin
	    MyFrame frametacgia=new MyFrame();
	    DefaultTableModel dftbtacgia=new DefaultTableModel();
	    dftbtacgia.addColumn("Mã tác giả");
	    dftbtacgia.addColumn("Tên tác giả");
	    dftbtacgia.addColumn("Quốc tịch");
        JTable tbtacgia=new JTable(dftbtacgia);
        JScrollPane sctacgia=new JScrollPane(tbtacgia,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frametacgia.add(sctacgia);
        frametacgia.pack();
        chitietthongtin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frametacgia.setVisible(true);
				frametacgia.setDefaultCloseOperation(1);
				try {
					prestatement=conn.prepareStatement("select*from author where author_id=?");
					prestatement.setInt(1,(int) tablesach.getValueAt(tablesach.getSelectedRow(),3));
					ResultSet rs=prestatement.executeQuery();
					dftbtacgia.setRowCount(0);
					while(rs.next()) {
						Vector<Object>vec=new Vector<Object>();
						vec.add(rs.getInt("author_id"));
						vec.add(rs.getString("author_name"));
						vec.add(rs.getString("nationality"));
						dftbtacgia.addRow(vec);
					}
				} catch (Exception e2) {}
				
			}
		});
        JMenuItem sua1=new JMenuItem("Sửa");
        sua1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					prestatement=conn.prepareStatement("update author set author_id=?,author_name=?,nationality=? where author_id=?");
					prestatement.setInt(1,(int) tbtacgia.getValueAt(tbtacgia.getSelectedRow(), 0));
					prestatement.setString(2,(String) tbtacgia.getValueAt(tbtacgia.getSelectedRow(), 1));
					prestatement.setString(3,(String) tbtacgia.getValueAt(tbtacgia.getSelectedRow(), 2));
					prestatement.setInt(4,(int) tbtacgia.getValueAt(tbtacgia.getSelectedRow(), 0));
					int x=prestatement.executeUpdate();
					if(x>0) {
						try {
							prestatement=conn.prepareStatement("select*from author where id=?");
							prestatement.setInt(1,(int) tbtacgia.getValueAt(tbtacgia.getSelectedRow(), 0));
							ResultSet rs=prestatement.executeQuery();
							Vector<Object>vec=new Vector<Object>();
							vec.add(rs.getInt("author_id"));
							vec.add(rs.getString("author_name"));
							vec.add(rs.getString("nationality"));
							dftbtacgia.addRow(vec);
							
						} catch (Exception e2) {}
						JOptionPane.showMessageDialog(null,"Sửa thành công");
					}
				} catch (Exception e2) {}
				
			}
		});
        JPopupMenu menutg=new JPopupMenu();
        menutg.add(sua1);
        tbtacgia.setComponentPopupMenu(menutg);
		//set layout
		con.add(pnnhaptk,BorderLayout.NORTH);
		con.add(pnsach,BorderLayout.CENTER);
		con.add(pnsort,BorderLayout.SOUTH);
		frame.add(con);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(1);
		
		
	}

	
}
