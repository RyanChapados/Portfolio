import urllib.request
from urllib.parse import urlparse
import ssl

from bs4 import BeautifulSoup
import urllib.robotparser
import time
import re
import json


def canonicalize(link, page=""):
    # Removes null links
    if link is None or link == "":
        return None

    # Removes tool links or fragment links or /
    if link[0] == "#" or link[0:8] == "//tools." or link == "/":
        return None

    # Removes links that were consistently in a different language
    if (link.find("barcelona.de") != -1 or page.find("barcelona.de") != -1) and \
            (link.find("/en/") == -1 and link.find("en/") == -1):
        return None

    # Removes zip and pdf links
    if link.find(".action") != -1 or link.find(".zip") != -1 or link.find(".pdf") != -1 or link.find(".jpg") != -1:
        return None

    # Classifies a relative link as one that does not contain a netloc
    link_parse = urlparse(link)
    if not bool(link_parse.netloc):
        page_url = urlparse(page)
        path = link_parse.path

        # Prevents weird errors idk
        if path == "":
            return None

        # Formats the relative link so that it can be parsed with the domain name
        if path[0:2] == "./" or path[0:2] == "//":
            path = path[1:]
        elif path[0] != "/":
            path = "/" + path

        l = page_url.scheme.lower() + "://" + page_url.netloc.lower() + path
    else:
        scheme = link_parse.scheme.lower() if link_parse.scheme.lower() != "" else "http"
        l = scheme + "://" + link_parse.netloc.lower() + link_parse.path + link_parse.params

    # Removes the fragment from the link
    fragment = l.find("#")
    l = l[:fragment] if fragment != -1 else l

    # Removes all the slashes from the link
    pr = l.find("//") + 1
    l = l[:pr + 1] + l[pr + 1:].replace("//", "/")

    # Removes port from link
    port = l.find(":", pr)
    if port != -1 and l[port + 1].isnumeric():
        slash = l.find("/", port)
        slash = len(l) if slash == -1 else slash
        l = l[:port] + l[slash:]

    return l


# print(canonicalize("/wiki/Artigas_Gardens", "https://en.wikipedia.org/wiki/List_of_Gaud%C3%AD_buildings"))
# f = urllib.request.urlopen("https://en.wikipedia.org/wiki/Antoni_Gaud%C3%AD",
# context=ssl.create_default_context(cafile=certifi.where()))
# s = BeautifulSoup(f.read(), 'html.parser')
# print(s)

# Magic line of code that magically protects against SSL errors by using magic (DO NOT REMOVE)
ssl._create_default_https_context = ssl._create_unverified_context


# Reads a url and writes the contents to a given file. Returns a list of outlinks
def read_url(url, outfile, show_html=False):
    f = urllib.request.urlopen(url)
    s = BeautifulSoup(f.read(), 'html.parser')

    if show_html:
        print(s)

    # Ensures the document is both html and english
    eng_nf = s.find(content="eng") is None and s.find(content="en") is None
    if s.html is None or (eng_nf and s.find(lang="en") is None) or \
            (eng_nf and s.find(content="de") is not None):
        print("Not html or not English")
        return None

    title_text = s.title.string if s.title is not None else ""

    # Creates and formats the page string by removing excess spaces and enters from the string
    page_str = f"<DOC>\n<DOCNO>{url}</DOCNO>\n<HEAD>{title_text}</HEAD>\n<TEXT>\n{str(s.get_text())}</TEXT>\n</DOC>\n"
    page_str = re.sub(' +', ' ', page_str)
    page_str = re.sub('\n+', '\n', page_str)

    # Writes the document to a file
    with open(outfile, "a", encoding="utf-8") as f:
        f.write(page_str)
        f.close()

    # Retrives all links from the url and canonicalizes them
    links = set()
    for link in s.find_all('a'):
        l = link.get('href')
        if l is not None:
            try:
                can = canonicalize(l, url)
            except:
                can = None

            if can is not None:
                links.add(can)

    return links


# Crawls through an ordered dict of urls, adding items as it goes. Crawls the given crawl_size in documents, and
# updates the list of urls every batch_size. Specifying an outfile results in every result being written to one file
# Not specifying scores results in only a single wave, and will cause an error if count > len(url_list)
def crawl(url_list, crawl_size, batch_size=500, outfile=None, scores={}, doc_no=1, inl={}, out={}, cr=set(), bc=0, c=0,
          fro={}):
    wave = url_list
    frontier = fro
    count = c
    batch_count = bc
    # rp = urllib.robotparser.RobotFileParser()
    # robot_list = {}
    docno = doc_no
    result_doc = "./docs/doc" + str(docno) + ".txt" if outfile is None else outfile
    crawled = cr
    outlinks = out
    inlinks = inl

    while count < crawl_size:
        # Checks to see if a wave has been completed, and if so sorts and moves to the next wave
        if scores and not wave:
            print("Finished wave, scoring next wave")
            wave, frontier = sort_urls(frontier, scores, batch_size)

        next_url = wave.pop()
        print(next_url)

        # Crawls the url if it has not already been crawled, otherwise adds it to the inlinks dictionary
        if next_url not in crawled:
            #next_dom = urlparse(next_url).netloc

            # Sees if the robots.txt needs to read again, otherwise waits for the delay
            #try:
            time.sleep(1)
                # Fetches the url if possible, otherwise throws the url away
                #if rp.can_fetch("*", next_url):
            try:
                new_urls = read_url(next_url, result_doc)

                # Ensures the new urls are valid, and if so adds them to the list and updates the count
                if new_urls is not None:

                    new_dict = {}
                    for li in new_urls:
                        # Creates a dict of urls and adds it to the next wave
                        if li not in crawled:
                            # Adds score to a link if it is already in new_dict
                            if li in new_dict:
                                new_dict[li] += 20
                            else:
                                new_dict[li] = 0

                        # Updates the inlinks dict
                        if li not in inlinks:
                            inlinks[li] = [next_url]
                        else:
                            inlinks[li].append(next_url)

                    outlinks[next_url] = list(new_dict.keys())
                    frontier.update(new_dict)
                    print("counted")
                    count += 1
                    batch_count += 1

                    # Updates the file that is being written to if that file has batch_size documents on it
                    if outfile is None and batch_count >= batch_size:
                        docno += 1
                        result_doc = "./docs/doc" + str(docno) + ".txt"
                        batch_count = 0

                        # Dumps data into files
                        dump_data(inlinks, outlinks, crawled, frontier)

            except:
                print("Link is broken")
            #except UnicodeDecodeError:
                #print("Error reading Robots.txt")
            #except:
                #print("Something went wrong and I am way too over it to fix it")
            #finally:
        crawled.add(next_url)

    # Ends by dumping the data
    dump_data(inlinks, outlinks, crawled, frontier)


# Reads the given file and parses it into an inverted index (I used the same code from assignment 2)
def read_all(filename):
    # reads the list of stopwords
    with open('stoplist.txt') as f:
        stop_words = f.readlines()

    # removes the \n from the lines
    for i in range(len(stop_words)):
        stop_words[i] = stop_words[i][:-1]

    nostem_index = {}
    length_list = []

    with open(filename, "r", encoding="ISO-8859-1", errors='ignore') as f:
        content = f.read()

        while '<DOC>' in content:
            docend = content.find('</DOC>')
            sub = content[:docend]
            docno_s = sub.find('<DOCNO>') + len('<DOCNO>')
            docno_e = sub.find('</DOCNO>')
            docno = sub[docno_s:docno_e].strip()
            text = ""

            while "<TEXT>" in sub:
                text_s = sub.find('<TEXT>') + len('<TEXT>')
                text_e = sub.find('</TEXT>')
                text = text + sub[text_s:text_e].strip() + "\n"
                sub = sub[text_e + len("</TEXT>"):]

            content = content[docend + len("</DOC>"):]

            # tokenizes the text
            tokens = tokenize(text, stop_words)
            length_list.append(len(tokens))

            nostem_index[docno] = doc_terms(tokens)

    return invert_index(nostem_index)


# Converts a string into a list of tokens
def tokenize(text, stop_words):
    doctext = text.lower()

    # removes punctuation
    replace = ['\n', ',', '\'', '`', '"', '-', '(', ')', '...', '..', ':', '?', '!', '_', ';', '=', '..', '}', '^', '[',
               ']', '/', '.']
    for j in replace:
        doctext = doctext.replace(j, " ")

    # removes stopwords
    doclist = doctext.split()
    nostops = []
    for j in doclist:
        if j not in stop_words:
            if len(j) > 1 and j[-1] == '.':
                nostops.append(j[:-1])
            else:
                nostops.append(j)

    return nostops


# Converts a list of terms into a dictionary of term positions
def doc_terms(doclist):
    term_dict = {}
    for q in range(len(doclist)):
        term = doclist[q]

        if term in term_dict:
            term_dict[term].append(q)
        else:
            term_dict[term] = [q]

    return term_dict


# converts document based index into an inverted index
def invert_index(doc_index):
    invert = {}
    for key in doc_index.keys():
        for val in doc_index[key]:
            if val not in invert:
                invert[val] = {}
            invert[val][key] = doc_index[key][val]

    return invert


# gets the document frequency of a given term dict
def doc_freq(term_dict):
    total = 0
    for i in term_dict.keys():
        total += len(term_dict[i])

    return total


# Creates a list of terms, and assigns scores to each
# Freq specifies the number of times a term must appear to be scored
def create_scores(freq, url_list=[]):
    if url_list:
        crawl(url_list, len(url_list), outfile="terms.txt")
    terms = read_all("terms.txt")
    doc_freqs = {term: doc_freq(terms[term]) for term in terms.keys() if doc_freq(terms[term]) > freq}

    return doc_freqs


# Scores and sorts each url in an OrderedDict of urls and scores
def sort_urls(url_dict, scores, batch_size):
    # Adds score to each url based on several criteria. The score may already be non-zero if there were inlinks
    for url in url_dict.keys():
        url_parse = urlparse(url)
        for (term, score) in scores.items():
            # Increase the score if the domain contains the score
            if url_parse.netloc.find(term) != -1:
                url_dict[url] += score

            # Increases the score if the path contains the score
            if url_parse.path.find(term) != -1:
                url_dict[url] += int(1.5 * score)

            # Increases the score if the domain is relevant
            if url_parse.netloc[-3:] == "gov" or url_parse.netloc[-3:] == "edu":
                url_dict[url] += 5

    # Iterates through the dict and sorts the score
    sorted_ = sorted(url_dict.items(), key=lambda x: x[1], reverse=True)

    # Returns the top batch_size urls as a list, and adds 20 to everything remaining in the dict
    return [u[0] for u in sorted_[:batch_size]], {k: v + 20 for k, v in sorted_[batch_size:]}


# Dumps the data from inlinks, outlinks, frontier, and crawled
def dump_data(inl, out, cr, front):
    print("dumping data")

    # Writes the current crawled to a file
    cr_f = open("crawled.txt", "w")
    cr_str = "\n".join(sorted(list(cr)))

    cr_f.write(cr_str)
    cr_f.close()

    # Writes the current batch of outlinks to a document
    with open("outlinks.json", "w") as f:
        json.dump(out, f)

    # Writes the current inlinks to a document
    with open("inlinks.json", "w") as in_f:
        json.dump(inl, in_f)

    # Writes the frontier to a file
    with open("frontier.json", "w") as fr_f:
        json.dump(front, fr_f)

    print("Writing complete")


# Reads incomplete outlinks, inlinks, and crawled allowing for a restart on failure
def read_restart():
    with open("outlinks.json", "r") as f:
        out = json.load(f)

    with open("inlinks.json", "r") as f:
        inl = json.load(f)

    cr = set()
    with open("crawled.txt", "r") as f:
        lines = f.readlines()
        for line in lines:
            cr.add(line[:-1])

    # Reads the frontier from a file
    with open("frontier.json", "r") as f:
        front = json.load(f)

    return out, inl, front, cr


od = ["http://en.wikipedia.org/wiki/Antoni_Gaud%C3%AD", "http://en.wikipedia.org/wiki/List_of_Gaud%C3%AD_buildings",
      "https://www.barcelona.de/en/barcelona-antoni-gaudi.html"]




scores = create_scores(30)
#crawl(od, 40000, 500, scores=scores)
o, i, fr, cra = read_restart()
crawl([], 40000, scores=scores, out=o, inl=i, cr=cra, batch_size=500, fro=fr, doc_no=78, c=38500)




