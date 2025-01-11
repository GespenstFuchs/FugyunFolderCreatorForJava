package fugyunfoldercreator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * カスタムメッセージボックス
 */
public class CustomMessageBox
{
	/**
	 * デフォルトボタン
	 */
	JButton defaultButton = null;

	/**
	 * メッセージボックス表示処理
	 * @param parent 親コンポーネント
	 * @param title タイトル
	 * @param message メッセージ（改行する場合、【<html><br>】を使用する。）
	 * @param messageType メッセージタイプ
	 * @param width 幅
	 * @param height 高さ
	 * @param buttonTextList ボタンテキストリスト
	 * @param defaultButtonIndex デフォルトボタンインデックス（０ＢＡＳＥ）
	 * @return ボタンに応じたインデックス
	 */
	public int showMessageBox(
			Component parent,
			String title,
			String message,
			int messageType,
			int width,
			int height,
			List<String> buttonTextList,
			int defaultButtonIndex)
	{
		// ダイアログフレームを生成する。
		JDialog dialog = new JDialog((Frame) null, title, true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setResizable(false);

		// サイズを設定する。
		dialog.setSize(new Dimension(width, height));

		// アイコンをリソースから読み込み、設定する。
		ImageIcon icon = new ImageIcon(getClass().getResource("/Icon.png"));
		dialog.setIconImage(icon.getImage());

		dialog.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// メッセージアイコン
		Icon messageIcon = UIManager.getIcon("OptionPane." + getMessageTypeString(messageType) + "Icon");
		JLabel iconLabel = new JLabel(messageIcon);

		gbc.insets = new Insets(0, 30, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		dialog.add(iconLabel, gbc);

		// メッセージ
		JLabel messageLabel = new JLabel();
		messageLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 14));
		messageLabel.setText(message);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 10, 0, 0);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		dialog.add(messageLabel, gbc);

		// ボタンパネルの作成する。
		JPanel buttonPanel = new JPanel(new FlowLayout());

		// 押下されたボタンのインデックスを格納するための配列
		int[] result = { -1 };

		// 生成したボタンを保持するリスト
		List<JButton> buttonList = new ArrayList<JButton>();

		// カレントインデックスを保持する配列
		int[] currentIndex = { -1 };

		// ボタンを生成する。
		for (int index = 0; index < buttonTextList.size(); index++)
		{
			JButton button = new JButton(buttonTextList.get(index));

			// 生成したボタンを保持する。
			buttonList.add(button);

			// インデックスを保持する。
			int buttonIndex = index;

			// ボタン押下処理
			button.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					result[0] = buttonIndex;
					dialog.dispose();
				}
			});

			// キー押下処理
			button.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyPressed(KeyEvent e)
				{
					// 押下されたキーのキーコードを判定する。
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						// Enterキーの場合
						result[0] = buttonIndex;
						dialog.dispose();
					}
					else
					{
						if (buttonTextList.size() > 1)
						{
							if (e.getKeyCode() == KeyEvent.VK_RIGHT)
							{
								currentIndex[0]++;
								if (buttonTextList.size() == currentIndex[0])
								{
									currentIndex[0] = 0;
								}

								buttonList.get(currentIndex[0]).requestFocus();
							}
							else if (e.getKeyCode() == KeyEvent.VK_LEFT)
							{
								currentIndex[0]--;
								if (-1 == currentIndex[0])
								{
									currentIndex[0] = buttonTextList.size() - 1;
								}

								buttonList.get(currentIndex[0]).requestFocus();
							}
						}
					}
				}
			});
			buttonPanel.add(button);

			// デフォルトボタンを設定する。
			if (defaultButtonIndex == index)
			{
				defaultButton = button;
				currentIndex[0] = index;
			}
		}

		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.gridwidth = 2;
		dialog.add(buttonPanel, gbc);

		// 親を設定する。
		// （ここで設定しないと、親の中央に表示されない。）
		dialog.setLocationRelativeTo(parent);

		// 画面表示後処理
		dialog.addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowOpened(java.awt.event.WindowEvent e)
			{
				// フォーカスを設定する。
				defaultButton.requestFocusInWindow();
			}
		});

		dialog.setVisible(true);

		// ボタンに応じたインデックスを返却する。
		return result[0];
	}

	/**
	 * メッセージタイプ文字列取得処理
	 * @param messageType メッセージタイプ
	 * @return　メッセージタイプ文字列
	 */
	private String getMessageTypeString(int messageType)
	{
		switch (messageType)
		{
			case JOptionPane.INFORMATION_MESSAGE:
				return "information";
			case JOptionPane.WARNING_MESSAGE:
				return "warning";
			case JOptionPane.ERROR_MESSAGE:
				return "error";
			default:
				return "question";
		}
	}
}
