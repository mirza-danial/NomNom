package com.example.danialmirza.nomnom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.danialmirza.nomnom.Utils.LoginActivity;
import com.example.danialmirza.nomnom.Utils.PostViewHolder;
import com.example.danialmirza.nomnom.Utils.saveUserData;
import com.example.danialmirza.nomnom.model.Post;
import com.example.danialmirza.nomnom.model.Restaurant;
import com.example.danialmirza.nomnom.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    //variable
    private RecyclerView mRecycler;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Post,PostViewHolder> mAdapter;
    private LinearLayoutManager mManager;

    private CircleImageView img_profile;
    private TextView U_name;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*
     * My Code starts from here
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]


        mRecycler = rootView.findViewById(R.id.rv_posts);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img_profile = view.findViewById(R.id.img_profile);
        U_name = view.findViewById(R.id.username);
        //Set username field and display user image in profile picture

        //AutoBackup
        String username;
        final String photo_path;

        DatabaseReference ref = mDatabase.child(getString(R.string.node_users)).child(getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);

                //AutoBackup using Google
                //Wasn't exactly sure what to backup since database is on Firebase and is loaded
                //locally(to show use of local database). It is greater than 25mb so can't be backed
                //up. Application user data was to be backed up so I backed up username and profile
                //photo path.
                saveUserData saveData = new saveUserData(getContext());
                saveData.saveData(u.getUsername() + "/n" + u.getPhoto_path());

                U_name.setText(u.getUsername());
                Picasso.get().load(u.getPhoto_path()).fit().error(R.drawable.profile_avatar_placeholder).into(img_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Ad banner code
        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");

        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsQuery, Post.class)
                .build();


        //Define Adapter
        mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {

            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PostViewHolder(inflater.inflate(R.layout.layout_mainfeed_post, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(PostViewHolder viewHolder, int position, final Post model) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                final String userId = model.getUser_id();
                final String restId = model.getRest_id();
                final PostViewHolder vholder = viewHolder;


                DatabaseReference ref = mDatabase.child(getString(R.string.node_users))
                        .child(userId);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //adds user to list there will be only one owner :)
                        final User owner = (dataSnapshot.getValue(User.class));
                        DatabaseReference ref = mDatabase.child(getString(R.string.node_restaurants))
                                .child(restId);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //adds user to list there will be only one owner :)
                                final Restaurant rest = (dataSnapshot.getValue(Restaurant.class));
                                View.OnClickListener upvoteListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View starView) {
                                        // Need to write to both places the post is stored
                                        DatabaseReference globalPostRef = mDatabase.child(getString(R.string.node_posts)).child(postRef.getKey());
                                        DatabaseReference userPostRef = mDatabase.child(getString(R.string.node_userposts)).child(model.getUser_id()).child(postRef.getKey());

                                        // Run two transactions
                                        onUpvoteClicked(globalPostRef);
                                        onUpvoteClicked(userPostRef);
                                    }
                                };
                                View.OnClickListener downvoteListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View starView) {
                                        // Need to write to both places the post is stored
                                        DatabaseReference globalPostRef = mDatabase.child(getString(R.string.node_posts)).child(postRef.getKey());
                                        DatabaseReference userPostRef = mDatabase.child(getString(R.string.node_userposts)).child(model.getUser_id()).child(postRef.getKey());

                                        // Run two transactions
                                        onDownvoteClicked(globalPostRef);
                                        onDownvoteClicked(userPostRef);
                                    }
                                };
                                View.OnClickListener imageListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View starView) {
                                        //Open Activity
                                        openViewPostActivity(owner,rest,model);
                                    }
                                };


                                // Bind Post to ViewHolder, setting OnClickListeners

                                vholder.bindToPost(model,owner,rest,upvoteListener,downvoteListener,imageListener );

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        };

        mRecycler.setAdapter(mAdapter);

    }



     public String getUid() {

        return FirebaseAuth.getInstance().getCurrentUser().getUid();
        //return HomeActivity.auth.getCurrentUser().getUid();
    }




    // [START post_stars_transaction]
    private void onUpvoteClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.getUpvoters().contains(getUid())) {
                    // UnUpvote the post and remove self from stars
                    p.setUpvotes(p.getUpvotes() - 1) ;
                    p.getUpvoters().remove(getUid());

                } else {
                    // upvote the post and add self to stars
                    p.setUpvotes(p.getUpvotes() + 1);
                    p.getUpvoters().add(getUid());
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

            }
        });
    }

    // [START post_stars_transaction]
    private void onDownvoteClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);

                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.getDownvoters().contains(getUid())) {
                    // UnUpvote the post and remove self from stars
                    p.setDownvotes(p.getUpvotes() - 1) ;
                    p.getDownvoters().remove(getUid());

                } else {
                    // upvote the post and add self to stars
                    p.setDownvotes(p.getUpvotes() + 1);
                    p.getDownvoters().add(getUid());
                }
                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }



    public Query getQuery(DatabaseReference databaseReference){
        return databaseReference.child(getString(R.string.node_userposts))
                .child(getUid());
    }


    void openViewPostActivity(User owner, Restaurant restaurant,Post post)
    {
        // Code to open new activity with intent goes here
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra("username",owner.getUsername());
        intent.putExtra("restaurant",restaurant.getName());
        intent.putExtra("rating",post.getRating());
        intent.putExtra("review",post.getReview());
        intent.putExtra("profile_imgage",owner.getPhoto_path());
        intent.putExtra("post_image",post.getPhoto_path());
        startActivity(intent);
    }

}
