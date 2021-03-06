package main;

import java.util.ArrayList;
import java.util.Scanner;

public class App {
	ArticleDao articleDao = new ArticleDao();
	ReplyDao replyDao = new ReplyDao();
	MemberDao memberDao = new MemberDao();
	
	Member loginedMember = null;
	
	public void start() {
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			if(loginedMember == null) {
				System.out.println("명령어 입력 : ");
			} else {
				System.out.println("명렁어 입력[" + loginedMember.getLoginId() + "(" + loginedMember.getNickname() + ") ] : ");
				
			}
			
			String cmd = sc.nextLine();
			
			if(cmd.contentEquals("exit")) {
				System.out.println("종료");
				break;
			}
			
			if(cmd.contentEquals("help")) {
				System.out.println("article [add: 게시물 추가 / list : 게시물 목록 조회 / read : 게시물 조회 / search : 검색]");
				System.out.println("member [signup : 회원가입 / signin : 로그인 / findpass : 비밀번호 찾기 / findid : 아이디 찾기 / logout : 로그아웃 / myinfo : 나의 정보 확인및 수정]");
			}
			
			if(cmd.contentEquals("article add")) {
				
				if(!isLogin()) {
					continue;
				}
				
				Article a = new Article();
				
				System.out.println("게시물 제목을 입력해주세요 . : ");
				String title = sc.nextLine();
				a.setTitle(title);
				
				System.out.println("게시물 내용을 입력해주세요. : ");
				String body = sc.nextLine();
				a.setBody(body);
				
				a.setNickname(loginedMember.getNickname());
				
				articleDao.insertArticle(a);
				System.out.println("게시물이 등록되었습니다.");
			}
			
			if(cmd.contentEquals("article update")) {
				
				System.out.println("수정할 게시물 선택 : ");
				int targetId = Integer.parseInt(sc.nextLine());
				
				Article target = articleDao.getArticleById(targetId);
				if(target == null) {
					System.out.println("없는 게시물입니다.");
				} else {
					
					System.out.println("게시물 제목을 입력해주세요. : ");
					String newTitle = sc.nextLine();
					target.setTitle(newTitle);
					
					System.out.println("게시물 내용을 입력해주세요. : ");
					String newBody = sc.nextLine();
					target.setBody(newBody);
					
					break;
				}
				
				if(cmd.equals("article delete")) {
					ArrayList<Article> articles = articleDao.getArticles();
					System.out.println("삭제할 게시물 선택 : ");
					int targetId = Integer.parseInt(sc.nextLine());
					Article target = articleDao.getArticleById(targetId);
					
					if(target == null) {
						System.out.println("게시물이 존재하지 않습니다.");
					} else {
						articleDao.removeArticle(target);
					}
					
				}
				
				if (cmd.equals("article read")) {
					System.out.println("상세보기할 게시물 선택 : ");
					int targetId = Integer.parseInt(sc.nextLine());
					Article target = articleDao.getArticleById(targetId);
					if (target == null) {
						System.out.println("게시물이 존재하지 않습니다.");
					} else {
						target.setHit(target.getHit() + 1);
						printArticle(target);

						while (true) {
							System.out.println("상세보기 기능을 선택해주세요(1. 댓글 등록, 2. 좋아요, 3. 수정, 4. 삭제, 5. 목록으로) :");
							int readCmd = Integer.parseInt(sc.nextLine());
							if (readCmd == 1) {
								Reply r = new Reply();

								System.out.println("댓글 내용을 입력해주세요:");
								String body = sc.nextLine();
								r.setParentId(target.getId());
								r.setBody(body);
								r.setNickname("익명");

								replyDao.insertReply(r);
								System.out.println("댓글이 등록되었습니다.");
								printArticle(target);

							} else if (readCmd == 2) {
								System.out.println("좋아요 기능");
							} else if (readCmd == 3) {
								System.out.println("수정 기능");
							} else if (readCmd == 4) {
								System.out.println("삭제 기능");
							} else if (readCmd == 5) {
								break;
							}
						}
					}
				}
				if (cmd.equals("article search")) {
					System.out.println("검색 항목을 (1. 제목, 2. 내용, 3. 제목 + 내용, 4. 작성자) : ");
					int flag = Integer.parseInt(sc.nextLine());
					System.out.println("검색 키워드를 입력해주세요 : ");
					String keyword = sc.nextLine();
					ArrayList<Article> searchedArticles;

					searchedArticles = articleDao.getSearchedArticlesByFlag(flag, keyword);

					printArticles(searchedArticles);
				}
				if (cmd.equals("member signup")) {
					System.out.println("======== 회원가입을 진행합니다.========");
					Member m = new Member();

					System.out.println("아이디를 입력해주세요 :");
					String id = sc.nextLine();
					m.setLoginId(id);

					System.out.println("비밀번호를 입력해주세요 :");
					String pw = sc.nextLine();
					m.setLoginPw(pw);

					System.out.println("닉네임을 입력해주세요 :");
					String nick = sc.nextLine();
					m.setNickname(nick);

					memberDao.insertMember(m);
					System.out.println("======== 회원가입이 완료되었습니다.========");
				}
				if (cmd.equals("member signin")) {
					System.out.println("아이디 :");
					String id = sc.nextLine();

					System.out.println("비밀번호 :");
					String pw = sc.nextLine();

					Member member = memberDao.getMemberByLoginIdAndLoginPw(id, pw);
					if (member == null) {
						System.out.println("비밀번호를 틀렸거나 잘못된 회원정보입니다.");
					} else {
						loginedMember = member;
						System.out.println(loginedMember.getNickname() + "님 안녕하세요!!");
					}

				}
				if (cmd.equals("member logout")) {

					if (!isLogin()) {
						continue;
					}

					loginedMember = null;
					System.out.println("로그아웃 되셨습니다.");

				}
			}
		}

		private void printArticles(ArrayList<Article> articleList) {
			for (int i = 0; i < articleList.size(); i++) {
				Article article = articleList.get(i);
				System.out.println("번호 : " + article.getId());
				System.out.println("제목 : " + article.getTitle());
				System.out.println("등록날짜 : " + article.getRegDate());
				System.out.println("작성자 : " + article.getNickname());
				System.out.println("조회수 : " + article.getHit());
				System.out.println("===================");
			}
		}

		private void printReplies(ArrayList<Reply> replyList) {
			for (int i = 0; i < replyList.size(); i++) {
				Reply reply = replyList.get(i);
				System.out.println("내용 : " + reply.getBody());
				System.out.println("작성자 : " + reply.getNickname());
				System.out.println("등록날짜 : " + reply.getRegDate());
				System.out.println("===================");
			}
		}

		private void printArticle(Article target) {
			System.out.println("==== " + target.getId() + " ====");
			System.out.println("번호 : " + target.getId());
			System.out.println("제목 : " + target.getTitle());
			System.out.println("내용 : " + target.getBody());
			System.out.println("등록날짜 : " + target.getRegDate());
			System.out.println("조회수 : " + target.getHit());
			System.out.println("===============");
			System.out.println("================댓글==============");

			ArrayList<Reply> replies = replyDao.getRepliesByParentId(target.getId());
			printReplies(replies);
		}

		private boolean isLogin() {
			if (loginedMember == null) {
				System.out.println("로그인이 필요한 기능입니다.");
				return false;
			} else {
				return true;
			}
		}
	}