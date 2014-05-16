#Rooted Random Walk

Walk on a rooted graph makes sure on every step there's a probability `p` that the walker will come back to root and start over. In other words, the walk is guaranteed to converge no matter where one starts. After some number of iterations we will get a stationary distribution over the whole graph.

I wrote some code for search keywords discovery. Given a brand, or a URL, what search keywords will most likely to lead to it? This is useful for brand awareness measurement.

#Input
 * search log file: tab separated 3 cols (query url frequency).
 * starting points: initial distribution over nodes. can be queries, URLs, or mixture of both
 * other parameters: number of iterations, `p` of coming to root, minimal frequency, etc.

#Output
|  Node | Relative probability |
| ---------- | ----------------|
| q_verizon | 5873.263398339937|
| u_verizonwireless.com | 1455.9084581603506|
|q_verizon wireless | 401.6830507740922|
|u_www.verizonwireless.com | 390.5675267279395|
|u_www.verizon.com/myverizon | 168.0251122644921|
|u_fios.verizon.com | 125.3538862114845|
|u_www.verizon.com | 103.63712605159769|
|q_verizon.com | 81.49887513592103|
|u_login.verizonwireless.com/amserver/ui/login  | 78.14558939858978|
|u_verizon.net | 75.8823853015666|
|q_verizon wireless my account  | 52.246465116221806|
|u_verizonwirelessdeals.com | 49.96107820664791|
|u_geo.yahoo.com/t | 41.8912042708351|
|q_verizonwireless.com | 23.877661355753848|
|u_verizon.com.bundlensave.com  | 23.017610430971754|
|q_my verizon | 20.610333293360657|
|u_verizonfiosbundles.com | 19.583158701515075|
|q_verizon.net | 19.35466362865535|
|q_my verizon account login | 18.106004623570044|
|q_verizon fios | 17.52378657847812| 
